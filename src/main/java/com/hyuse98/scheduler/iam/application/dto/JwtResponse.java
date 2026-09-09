package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record JwtResponse(
        @Schema(description = "JWT token generated for the session", example = "eyJhbGciOiJIUzM4NCJ9...")
        String token,
        @Schema(description = "Refresh token used to obtain a new JWT token", example = "a2f4...")
        String refreshToken
) {}
