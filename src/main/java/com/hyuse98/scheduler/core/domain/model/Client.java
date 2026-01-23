package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Client {

    private final UUID id;
    private String email;
    private String name;
    private String phoneNumber;
    private Date birthday;
    private String address;
    private String cns;
    private final Date createdAt;
    private Boolean isActive;

    private Client(UUID id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    private Client(UUID id, String email, String name, String phoneNumber, Date birthday, String address, String cns, Date createdAt, Boolean isActive) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.cns = cns;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public static Client create(UUID id, String email, String name, String phoneNumber, Date birthday, String address, String CNS, Date createdAt, Boolean isActive) {
        return new Client(id, email, name, phoneNumber, birthday, address, CNS, createdAt, isActive);
    }

    public static Client reconstitute(UUID id, String name, String email, String phoneNumber, Date birthday, String address, String CNS, Date createdAt, Boolean isActive) {
        return new Client(id, name, email, phoneNumber, birthday, address, CNS, createdAt, isActive);
    }

    public void validate() {
        Objects.requireNonNull(name, "Full name is Required");
        Objects.requireNonNull(phoneNumber, "Phone Number is required");
        Objects.requireNonNull(birthday, "Birthday is Required");
        Objects.requireNonNull(address, "Address is Required");
        Objects.requireNonNull(cns, "CNS is Required");
    }

    public void updateProfile(String name, String phoneNumber, Date birthday, String address, String cns) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.cns = cns;
    }

    public Client disable() {
        this.isActive = false;
        return this;
    }

    public Client enable() {
        this.isActive = true;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getCns() {
        return cns;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }
}
