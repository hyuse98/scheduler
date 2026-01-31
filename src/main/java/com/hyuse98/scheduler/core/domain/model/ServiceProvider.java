package com.hyuse98.scheduler.core.domain.model;

import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ServiceProviderJpaEntity;

import java.util.Date;
import java.util.UUID;

public class ServiceProvider{

    private final UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private String address;
    private String expertise;
    private String registry;
    private final Date createdAt;
    private Boolean isActive;

    private ServiceProvider() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
    }

    private ServiceProvider(UUID id, String name, String email, String phoneNumber, Date birthday, String address, String expertise, String registry, Date createdAt, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.expertise = expertise;
        this.registry = registry;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }
}
