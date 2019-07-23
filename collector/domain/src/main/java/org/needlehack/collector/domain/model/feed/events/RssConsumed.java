package org.needlehack.collector.domain.model.feed.events;

import org.needlehack.collector.domain.events.DomainEvent;
import org.needlehack.collector.domain.model.feed.Feed;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class RssConsumed extends DomainEvent {

    private final Feed feed;

    public RssConsumed(Feed feed) {
        super(feed.getSource(), Date.from(Instant.now()));
        this.feed = feed;
    }

    public Feed getFeed() {
        return feed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssConsumed rssConsumed = (RssConsumed) o;
        return Objects.equals(feed, rssConsumed.feed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feed);
    }

    @Override
    public String toString() {
        return "RssConsumed{" + "feed=" + feed + '}';
    }
}
