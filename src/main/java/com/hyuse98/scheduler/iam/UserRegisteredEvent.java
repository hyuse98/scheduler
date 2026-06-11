package com.hyuse98.scheduler.iam;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

@Externalized("user.registered")
public record UserRegisteredEvent(
        UUID userId,
        String userEmail
) {
}
