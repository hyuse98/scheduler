package com.hyuse98.scheduler.core.domain.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Client extends User {

    private String CNS;

    private Client() {
    }

    private Client(UUID id, String email, String password, String phoneNumber, String name, Date birthday, String address, Date createdAt, Boolean isActive, String CNS) {
        super(id, email, password, phoneNumber, name, birthday, address, createdAt, isActive);
        this.CNS = CNS;
        validate();
    }

    public void validate() {
        Objects.requireNonNull(email, "Email address is required");
        Objects.requireNonNull(password, "Password is required");
        Objects.requireNonNull(phoneNumber, "Phone Number is required");
        Objects.requireNonNull(name, "Full name is Required");
        Objects.requireNonNull(birthday, "Birthday is Required");
        Objects.requireNonNull(address, "Address is Required");
        Objects.requireNonNull(CNS, "CNS is Required");
    }

    /**
     * Factory method to create new instance of Client
     */

    public static Client create(UUID id, String email, String password, String phoneNumber, String name, Date birthday, String address, Date createdAt, Boolean isActive, String CNS) {
        return new Client(id, email, password, phoneNumber, name, birthday, address, createdAt, isActive, CNS);
    }

    /**
     * Factory method to create an instance of Client from Database
     */

    public static Client reconstitute(UUID id, String email, String password, String phoneNumber, String name, Date birthday, String address, Date createdAt, Boolean isActive, String CNS) {
        return new Client(id, email, password, phoneNumber, name, birthday, address, createdAt, isActive, CNS);
    }

    /**
     * Method to change password
     * @param password
     */

    public void changePassword(String password) {
        this.password = Objects.requireNonNull(password, "New password cannot be null");
    }

    /**
     * This method turn Client inactive
     */

    public Client disable() {
        this.isActive = false;
        return this;
    }

    /**
     * This method turn Client active
     */

    public Client enable() {
        this.isActive = true;
        return this;
    }

    public String getCNS() {
        return CNS;
    }
}
