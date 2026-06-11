package com.hyuse98.scheduler.core.infrastructure.messaging.dto;

import java.util.UUID;

public record UserRegisteredMessage(
        UUID userId,
        String userEmail
) {
}
