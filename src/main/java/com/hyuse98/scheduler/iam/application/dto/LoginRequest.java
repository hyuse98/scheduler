package com.hyuse98.scheduler.iam.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "E-mail de acesso do usuário", example = "name@hotmail.com")
        String email,

        @Schema(description = "Senha de acesso", example = "Senha123456789")
        String password
) {}