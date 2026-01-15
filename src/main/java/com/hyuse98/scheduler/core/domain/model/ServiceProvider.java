package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ServiceProviderEntity}
 */
public class ServiceProvider extends User{

    String expertise;
    String registry;

    public ServiceProvider() {
    }

    public ServiceProvider(UUID id, String email, String password, String phoneNumber, String name, Date birthday, String address, Date createdAt, Boolean isActive, String expertise, String registry) {
        super(id, email, password, phoneNumber, name, birthday, address, createdAt, isActive);
        this.expertise = expertise;
        this.registry = registry;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getRegistry() {
        return registry;
    }
}
