package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserProfileResponse(
        @Schema(description = "User UUID", example = "a6315675-69df-44ba-a37d-39e54536b8b3")
        UUID id,
        @Schema(description = "User email", example = "name@hotmail.com")
        String email,
        @Schema(description = "User role ", example = "ROLE_USER")
        String role
) {
}
