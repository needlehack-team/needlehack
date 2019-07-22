package org.needlehack.collector.domain.model.feed.events;

import org.needlehack.collector.domain.model.feed.FeedItem;

public class AppendedItem extends FeedEvent {

    public AppendedItem(FeedItem feedItem) {
        super(feedItem);
    }
}
