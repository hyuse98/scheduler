package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class ServiceProvider {

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

    private ServiceProvider(UUID id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
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

    public static ServiceProvider create(UUID id, String name, String email, String phoneNumber, Date birthday, String address, String expertise, String registry, Date createdAt, Boolean isActive) {
        return new ServiceProvider(id, name, email, phoneNumber, birthday, address, expertise, registry, createdAt, isActive);
    }

    public static ServiceProvider reconstitute(UUID id, String name, String email, String phoneNumber, Date birthday, String address, String expertise, String registry, Date createdAt, Boolean isActive) {
        return new ServiceProvider(id, name, email, phoneNumber, birthday, address, expertise, registry, createdAt, isActive);
    }

    public void validate() {
        Objects.requireNonNull(name, "Full name is Required");
        Objects.requireNonNull(email, "Email is Required");
        Objects.requireNonNull(expertise, "Expertise is Required");
        Objects.requireNonNull(registry, "Registry (CRM, CRP, etc) is Required");
    }

    public void updateProfile(String name, String phoneNumber, Date birthday, String address, String expertise, String registry) {
        if (name != null && !name.isBlank()) this.name = name;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (birthday != null) this.birthday = birthday;
        if (address != null && !address.isBlank()) this.address = address;
        if (expertise != null && !expertise.isBlank()) this.expertise = expertise;
        if (registry != null && !registry.isBlank()) this.registry = registry;
    }

    public ServiceProvider disable() {
        this.isActive = false;
        return this;
    }

    public ServiceProvider enable() {
        this.isActive = true;
        return this;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Date getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getExpertise() { return expertise; }
    public String getRegistry() { return registry; }
    public Date getCreatedAt() { return createdAt; }
    public Boolean getIsActive() { return isActive; }
}