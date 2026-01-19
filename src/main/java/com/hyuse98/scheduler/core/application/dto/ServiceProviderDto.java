package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.hyuse98.scheduler.core.domain.model.ServiceProvider}
 */
public record ServiceProviderDto(String expertise, String registry, UUID id, String email, String password,
                                 String phoneNumber, String name, Date birthday, String address, Date createdAt,
                                 Boolean isActive) implements Serializable {
}