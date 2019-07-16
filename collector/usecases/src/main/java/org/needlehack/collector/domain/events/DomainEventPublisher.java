package org.needlehack.collector.domain.events;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
