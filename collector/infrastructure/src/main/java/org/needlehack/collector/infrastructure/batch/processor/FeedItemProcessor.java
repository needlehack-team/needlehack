package org.needlehack.collector.infrastructure.batch.processor;

import com.rometools.rome.feed.synd.SyndEntry;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.infrastructure.converter.FeedItemConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map.Entry;

@Component
public class FeedItemProcessor implements ItemProcessor<Entry<Feed, SyndEntry>, FeedItem> {

    private static final Logger logger = LoggerFactory.getLogger(FeedItemProcessor.class);

    @Override
    public FeedItem process(final Entry<Feed, SyndEntry> feed) throws Exception {

        FeedItem feedItem = null;
        
        try {
            feedItem = FeedItemConverter.convertFeedToPost(feed.getKey(), feed.getValue());
        } catch (Exception exc) {
            logger.error(
                    "An error has been produced at " + feed.getValue()
                            .getLink() + " in processor method",
                    exc);
        }

        return feedItem;
    }
}
