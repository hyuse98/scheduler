package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationRequest(
        @Schema(description = "User access email", example = "name@hotmail.com")
        String email,

        @Schema(description = "User access password", example = "Senha123456789")
        String password
) {}
