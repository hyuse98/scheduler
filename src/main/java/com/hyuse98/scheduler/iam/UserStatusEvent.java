package com.hyuse98.scheduler.iam;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

@Externalized
public record UserStatusEvent(
        UUID id,
        boolean isActive
) {
}
