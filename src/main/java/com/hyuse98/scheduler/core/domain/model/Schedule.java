package com.hyuse98.scheduler.core.domain.model;

import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ScheduleJpaEntity;

import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link ScheduleJpaEntity}
 */
public class Schedule {

    private final UUID id;
    private UUID clientId;
    private UUID serviceProviderId;
    private String serviceType;
    private String description;
    private final Date scheduledAt;
    private String status;

    private Schedule(UUID id, Date scheduledAt) {
        this.id = id;
        this.scheduledAt = scheduledAt;
    }

    private Schedule(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description, Date scheduledAt, String status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceProviderId = serviceProviderId;
        this.serviceType = serviceType;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.status = status;
    }
}
