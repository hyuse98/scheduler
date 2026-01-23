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

    public ServiceProvider(UUID id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public ServiceProvider(UUID id, String name, String email, String phoneNumber, Date birthday, String address, Date createdAt, Boolean isActive, String expertise, String registry) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.expertise = expertise;
        this.registry = registry;
    }
}
