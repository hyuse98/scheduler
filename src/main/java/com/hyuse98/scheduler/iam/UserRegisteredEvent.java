package com.hyuse98.scheduler.iam;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String userEmail
) {
}
