package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;

public record UpdateServiceProviderRequest(
        String name,
        String phoneNumber,
        Date birthday,
        String address,
        String expertise,
        String registry
) implements Serializable {
}