package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.hyuse98.scheduler.core.domain.model.Schedule}
 */
public record ScheduleDto(UUID id, UUID clientId, UUID serviceProviderId, String serviceType, String description,
                          Date scheduledAt, String status) implements Serializable {
}