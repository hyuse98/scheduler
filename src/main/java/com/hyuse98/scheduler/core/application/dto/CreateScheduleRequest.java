package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record CreateScheduleRequest(
        UUID clientId,
        UUID serviceProviderId,
        String serviceType,
        String description,
        Date scheduledAt
) implements Serializable {
}