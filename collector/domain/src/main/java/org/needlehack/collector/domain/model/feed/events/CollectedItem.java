package org.needlehack.collector.domain.model.feed.events;

import org.needlehack.collector.domain.model.feed.FeedItem;

public class CollectedItem extends FeedEvent {

    public CollectedItem(FeedItem feedItem) {
        super(feedItem);
    }
}
