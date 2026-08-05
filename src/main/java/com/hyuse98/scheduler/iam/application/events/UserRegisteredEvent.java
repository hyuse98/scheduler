package com.hyuse98.scheduler.iam.application.events;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

/**
 * Used by RegisterUserUseCaseImpl to send event of registration
 */
@Externalized("user.registered")
public record UserRegisteredEvent(
        UUID userId,
        String userEmail
) {
}
