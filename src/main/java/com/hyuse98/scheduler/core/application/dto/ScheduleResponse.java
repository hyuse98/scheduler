package com.hyuse98.scheduler.core.application.dto;

import java.util.Date;
import java.util.UUID;

public record ScheduleResponse(
        UUID id,
        UUID clientId,
        UUID serviceProviderId,
        String serviceType,
        String description,
        Date scheduledAt,
        String status
) {
}