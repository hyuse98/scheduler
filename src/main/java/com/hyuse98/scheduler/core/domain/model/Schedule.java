package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ScheduleEntity}
 */
public class Schedule {

    UUID id;
    UUID clientId;
    UUID serviceProviderId;
    String serviceType;
    String description;
    Date scheduledAt;
    String status;

    public Schedule() {
    }

    public Schedule(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description, Date scheduledAt, String status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceProviderId = serviceProviderId;
        this.serviceType = serviceType;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getServiceProviderId() {
        return serviceProviderId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public Date getScheduledAt() {
        return scheduledAt;
    }

    public String getStatus() {
        return status;
    }
}
