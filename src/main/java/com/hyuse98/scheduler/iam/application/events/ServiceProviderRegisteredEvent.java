package com.hyuse98.scheduler.iam.application.events;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

/**
 * Used by RegisterServiceProviderUseCaseImpl to send event of registration
 */
@Externalized("provider.registered")
public record ServiceProviderRegisteredEvent(
        UUID userId,
        String userEmail
) {
}
