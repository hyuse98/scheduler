package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record CreateServiceProviderRequest(
        UUID id,
        String email,
        String name,
        String phoneNumber,
        Date birthday,
        String address,
        String expertise,
        String registry,
        Date createdAt,
        Boolean isActive
) implements Serializable {
}