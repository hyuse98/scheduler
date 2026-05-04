package com.hyuse98.scheduler.iam.application.dto;

public record LoginRequest(
        String email,
        String password
) {}
