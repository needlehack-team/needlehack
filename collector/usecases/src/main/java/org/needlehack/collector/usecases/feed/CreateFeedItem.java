package org.needlehack.collector.usecases.feed;

import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.exceptions.DuplicateEntityException;
import org.needlehack.collector.domain.exceptions.EntityAlreadyExistsException;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.needlehack.collector.domain.model.feed.events.CreatedItem;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.feed.params.CreatingFeedItemParams;

public class CreateFeedItem implements UseCase<CreatingFeedItemParams> {

    private final FeedRepository feedRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CreateFeedItem(FeedRepository feedRepository, DomainEventPublisher domainEventPublisher) {
        this.feedRepository = feedRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public FeedItem execute(CreatingFeedItemParams params) {

        FeedItem feedItem = params.createObject();

        try {
            feedItem = feedRepository.create(feedItem);
            domainEventPublisher.publish(new CreatedItem(feedItem));
        } catch (EntityAlreadyExistsException exception) {
            throw new DuplicateEntityException(String.format("The feedItem [%s] already exists", feedItem.getTitle()),
                    exception);
        }

        return feedItem;
    }
}
