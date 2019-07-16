package org.needlehack.collector.infrastructure.events.subscribers;

import org.needlehack.collector.domain.events.DomainEvent;
import org.needlehack.collector.domain.events.DomainEventSubscriber;
import org.needlehack.collector.domain.model.feed.events.CollectedItem;
import org.needlehack.collector.domain.model.feed.events.CreatedItem;
import org.needlehack.collector.usecases.feed.CreateFeedItem;
import org.needlehack.collector.usecases.feed.params.CreatingFeedItemParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedStatusSubscriber implements DomainEventSubscriber {

    @Autowired
    private CreateFeedItem createFeedItem;

    @Override
    public void process(DomainEvent domainEvent) {

        // TODO REFACTOR - https://dzone.com/articles/instanceof-considered-harmful

        if (domainEvent instanceof CollectedItem) {
            process((CollectedItem) domainEvent);
        } else if (domainEvent instanceof CreatedItem) {
            process((CreatedItem) domainEvent);
        }
    }

    private void process(CollectedItem event) {
        // TODO Nothing to do here
    }
}
