package org.needlehack.collector.usecases.feed;

import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.model.feed.ExtractorService;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.events.AppendedItem;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.feed.params.AppendingFeedItemParams;

public class AppendFeedItem implements UseCase<AppendingFeedItemParams> {

    private final ExtractorService extractorService;
    private final DomainEventPublisher domainEventPublisher;

    public AppendFeedItem(ExtractorService extractorService, DomainEventPublisher domainEventPublisher) {
        this.extractorService = extractorService;
        this.domainEventPublisher = domainEventPublisher;
    }

    public FeedItem execute(AppendingFeedItemParams params) {

        FeedItem feedItem = params.createObject();

        feedItem = extractorService.insertContentAndTopics(feedItem);
        domainEventPublisher.publish(new AppendedItem(feedItem));

        return feedItem;
    }
}
