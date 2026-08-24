package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "User access email", example = "name@hotmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "User access password", example = "Senha123456789")
        @NotBlank(message = "Password is required")
        String password
) {}