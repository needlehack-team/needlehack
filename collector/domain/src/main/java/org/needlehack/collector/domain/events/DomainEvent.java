package org.needlehack.collector.domain.events;

import java.util.Date;
import java.util.UUID;

public abstract class DomainEvent {

    private final Date happenedAt;
    private final String id;
    private final String entityId;


    public DomainEvent(String id, String entityId, Date happenedAt) {
        this.id = id;
        this.entityId = entityId;
        this.happenedAt = happenedAt;
    }

    public DomainEvent(String entityId, Date happenedAt) {
        this(UUID.randomUUID().toString(), entityId, happenedAt);
    }

    public String getId() {
        return id;
    }

    public String getEntityId() {
        return entityId;
    }

    public Date getHappenedAt() {
        return happenedAt;
    }

}
