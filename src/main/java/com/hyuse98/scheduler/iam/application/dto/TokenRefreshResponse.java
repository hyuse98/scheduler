package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRefreshResponse(
        @Schema(description = "New JWT token generated", example = "eyJhbGciOiJIUzM4NCJ9...")
        String accessToken,
        @Schema(description = "Refresh token to reuse if needed or generate again", example = "a2f4...")
        String refreshToken
) {}
