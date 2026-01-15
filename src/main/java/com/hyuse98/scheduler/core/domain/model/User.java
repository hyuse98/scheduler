package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.UUID;

public class User {

    protected UUID id;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected String name;
    protected Date birthday;
    protected String address;
    protected Date createdAt;
    protected Boolean isActive;

    protected User() {
    }

    protected User(UUID id, String email, String password, String phoneNumber, String name, Date birthday, String address, Date createdAt, Boolean isActive) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }
}
