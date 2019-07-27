package org.needlehack.collector.infrastructure.runner;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.events.RssConsumed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Component
public class OpmlReader {

    private static final Logger logger = LoggerFactory.getLogger(OpmlReader.class);


    @Value("classpath:opml/engineering_blogs.opml")
    private Resource resourceOpml;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public void consumeOpml() {

        WireFeedInput input = new WireFeedInput();
        List<Outline> outlines = null;
        try {
            Opml feed = (Opml) input.build(new InputSource(resourceOpml.getInputStream()));
            outlines = feed.getOutlines();
        } catch (IllegalArgumentException | IOException | FeedException exc) {
            logger.error("An error has been produced while opml was processed", exc);
        }

        List<Outline> outlinesToRead = CollectionUtils.isEmpty(outlines) ? Collections.emptyList()
                : outlines.get(0)
                .getChildren();

        outlinesToRead
                .forEach(outlineForFeed -> {
                    try {
                        domainEventPublisher.publish(new RssConsumed(new Feed(new URL(outlineForFeed.getXmlUrl()), outlineForFeed.getText())));
                    } catch (MalformedURLException exc) {
                        logger.error("Url of feed [{}]", outlineForFeed.getText());
                        logger.error("An error has been produced while opml was processed.", exc);
                    }
                });

    }
}
