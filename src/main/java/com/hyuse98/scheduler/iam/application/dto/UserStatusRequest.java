package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object to change a user's activation status")
public record UserStatusRequest(

        @Schema(
                description = "Determines whether user access should be enabled (true) or disabled (false).",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "The activation status (active) is mandatory.")
        Boolean active
) {}