package org.needlehack.collector.infrastructure.events;

import org.needlehack.collector.domain.events.DomainEvent;
import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.events.DomainEventSubscriber;
import org.needlehack.collector.infrastructure.events.subscribers.FeedStatusSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DomainEventInMemoryPublisher implements DomainEventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(DomainEventInMemoryPublisher.class);

    @Autowired
    private FeedStatusSubscriber feedStatusSubscriber;

    // TODO Include different types of subscribers

    private List<DomainEventSubscriber> subscribers;

    public DomainEventInMemoryPublisher() {
        this.subscribers = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        addSubscriber(feedStatusSubscriber);
    }

    @Override
    public void publish(DomainEvent event) {
        this.subscribers.forEach((subscriber) -> {
            LOG.trace(" > publishing {} to {}", event.getClass()
                            .getSimpleName(),
                    subscriber.getClass()
                            .getSimpleName());
            subscriber.process(event);
        });
    }

    public void addSubscriber(DomainEventSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }
}
