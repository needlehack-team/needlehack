package org.needlehack.collector.infrastructure.batch.writer;

import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.feed.params.CreatingFeedItemParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedItemSender implements ItemWriter<FeedItem> {

    private static final Logger logger = LoggerFactory.getLogger(FeedItemSender.class);

    @Autowired
    private UseCase<CreatingFeedItemParams> createFeedItem;

    @Override
    public void write(List<? extends FeedItem> feedItems) {
        feedItems.forEach(feedItem -> createFeedItem.execute(new CreatingFeedItemParams(feedItem)));
    }
}
