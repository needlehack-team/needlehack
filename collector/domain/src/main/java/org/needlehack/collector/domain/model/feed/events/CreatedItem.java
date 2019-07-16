package org.needlehack.collector.domain.model.feed.events;

import org.needlehack.collector.domain.model.feed.FeedItem;

public class CreatedItem extends FeedEvent {

    public CreatedItem(FeedItem feedItem) {
        super(feedItem);
    }
}
