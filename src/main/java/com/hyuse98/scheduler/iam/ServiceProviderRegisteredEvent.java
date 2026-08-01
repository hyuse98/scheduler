package com.hyuse98.scheduler.iam;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

@Externalized("provider.registered")
public record ServiceProviderRegisteredEvent(
        UUID userId,
        String userEmail
) {
}
