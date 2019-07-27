package org.needlehack.collector.usecases.rss;

import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.events.CollectedItem;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.rss.params.CollectingFeedParams;

import java.util.List;

public class CollectFeed implements UseCase<CollectingFeedParams> {

    private final FeedListener feedListener;
    private final DomainEventPublisher domainEventPublisher;

    public CollectFeed(FeedListener feedListener, DomainEventPublisher domainEventPublisher) {
        this.feedListener = feedListener;
        this.domainEventPublisher = domainEventPublisher;
    }

    public List<FeedItem> execute(CollectingFeedParams params) {

        Feed feed = params.createObject();

        List<FeedItem> feedItemsCollected = feedListener.extract(feed);
        feedItemsCollected.forEach(feedItem -> domainEventPublisher.publish(new CollectedItem(feedItem)));

        return feedItemsCollected;
    }
}
