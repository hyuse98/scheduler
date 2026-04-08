package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;

public record CreateServiceProviderRequest(
        String email,
        String name,
        String phoneNumber,
        Date birthday,
        String address,
        String expertise,
        String registry
) implements Serializable {
}