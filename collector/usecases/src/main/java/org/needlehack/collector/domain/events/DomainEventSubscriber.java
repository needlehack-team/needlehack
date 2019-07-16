package org.needlehack.collector.domain.events;

public interface DomainEventSubscriber {

    void process(DomainEvent event);
}
