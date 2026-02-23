package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Schedule {

    private final UUID id;
    private UUID clientId;
    private UUID serviceProviderId;
    private String serviceType;
    private String description;
    private final Date scheduledAt;
    private ScheduleStatus status;

    private Schedule(UUID id, Date scheduledAt) {
        this.id = id;
        this.scheduledAt = scheduledAt;
    }

    private Schedule(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description, Date scheduledAt, ScheduleStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceProviderId = serviceProviderId;
        this.serviceType = serviceType;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.status = status;
    }

    public static Schedule create(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description, Date scheduledAt) {
        return new Schedule(id, clientId, serviceProviderId, serviceType, description, scheduledAt, ScheduleStatus.PENDING);
    }

    public static Schedule reconstitute(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description, Date scheduledAt, String status) {
        ScheduleStatus parsedStatus = status != null ? ScheduleStatus.valueOf(status) : ScheduleStatus.PENDING;
        return new Schedule(id, clientId, serviceProviderId, serviceType, description, scheduledAt, parsedStatus);
    }

    public void validate() {
        Objects.requireNonNull(clientId, "Client ID is required");
        Objects.requireNonNull(serviceProviderId, "Service Provider ID is required");
        Objects.requireNonNull(scheduledAt, "Scheduled Date is required");

        if (scheduledAt.before(new Date())) {
            throw new IllegalArgumentException("Cannot schedule an appointment in the past");
        }
    }

    public void updateDetails(String serviceType, String description) {
        if (serviceType != null && !serviceType.isBlank()) this.serviceType = serviceType;
        if (description != null && !description.isBlank()) this.description = description;
    }

    public void changeStatus(ScheduleStatus newStatus) {
        Objects.requireNonNull(newStatus, "Status cannot be null");
        this.status = newStatus;
    }

    public UUID getId() { return id; }
    public UUID getClientId() { return clientId; }
    public UUID getServiceProviderId() { return serviceProviderId; }
    public String getServiceType() { return serviceType; }
    public String getDescription() { return description; }
    public Date getScheduledAt() { return scheduledAt; }
    public ScheduleStatus getStatus() { return status; }
}