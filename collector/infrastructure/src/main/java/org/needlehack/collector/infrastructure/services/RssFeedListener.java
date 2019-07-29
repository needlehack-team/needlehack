package org.needlehack.collector.infrastructure.services;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.infrastructure.converter.FeedItemConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RssFeedListener implements FeedListener {

    private final static Logger log = LoggerFactory.getLogger(RssFeedListener.class);

    @Override
    public List<FeedItem> extract(final Feed feed) {

        log.info("Extracting RSS from feed [{}]", feed.getSource());

        List<FeedItem> postsCollected = new ArrayList<>();

        try {

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feedLoaded = input.build(new XmlReader(feed.getUri()));

            postsCollected.addAll(feedLoaded.getEntries()
                    .stream()
                    .map(entry -> FeedItemConverter.convertFeedToPost(feed, entry))
                    .collect(Collectors.toList()));

        } catch (Exception exception) {
            log.error("An error has been produced when the feed from source [{}] was loaded", feed.getSource());
        }

        return postsCollected;
    }
}
