package org.needlehack.collector.domain.model.feed.events;

import org.needlehack.collector.domain.events.DomainEvent;
import org.needlehack.collector.domain.model.feed.FeedItem;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class FeedEvent extends DomainEvent {

    private final FeedItem feedItem;

    public FeedEvent(FeedItem feedItem) {
        super(feedItem.getGeneratedId(), Date.from(Instant.now()));
        this.feedItem = feedItem;
    }

    public FeedItem getFeedItem() {
        return feedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedEvent feedEvent = (FeedEvent) o;
        return Objects.equals(feedItem, feedEvent.feedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedItem);
    }

    @Override
    public String toString() {
        return "FeedEvent{" + "feedItem=" + feedItem + '}';
    }
}
