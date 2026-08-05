package com.hyuse98.scheduler.iam.infrastructure.api.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Standard format for API error responses")
public record ErrorResponse(

        @Schema(description = "Exact date and time of the error", example = "2026-08-01T01:22:13.123")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        Integer status,

        @Schema(description = "HTTP error name", example = "Bad Request")
        String error,

        @Schema(description = "Detailed message for the user or developer", example = "Incorrect email or password")
        String message
) {}