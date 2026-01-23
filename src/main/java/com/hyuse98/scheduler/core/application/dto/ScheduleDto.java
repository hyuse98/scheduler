package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record ScheduleDto(UUID id,
                          UUID clientId,
                          UUID serviceProviderId,
                          String serviceType,
                          String description,
                          Date scheduledAt,
                          String status) implements Serializable {
}