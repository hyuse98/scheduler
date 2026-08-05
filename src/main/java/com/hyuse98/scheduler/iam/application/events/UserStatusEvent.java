package com.hyuse98.scheduler.iam.application.events;

import org.springframework.modulith.events.Externalized;

import java.util.UUID;

/**
 * Used by UpdateUserStatusUseCaseImpl to send event of status updated
 */
//TODO(Check queue description later core.user.active.queue)
@Externalized("core.user.active.queue")
public record UserStatusEvent(
        UUID id,
        boolean isActive
) {
}
