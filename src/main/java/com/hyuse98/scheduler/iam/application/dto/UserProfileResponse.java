package com.hyuse98.scheduler.iam.application.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String role
) {
}
