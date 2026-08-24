package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateScheduleRequest(
        @NotNull(message = "Client ID is required")
        UUID clientId,
        @NotNull(message = "Service Provider ID is required")
        UUID serviceProviderId,
        @NotBlank(message = "Service Type is required")
        String serviceType,
        String description,
        @NotNull(message = "Scheduled date is required")
        @Future(message = "Scheduled date must be in the future")
        Date scheduledAt
) implements Serializable {
}