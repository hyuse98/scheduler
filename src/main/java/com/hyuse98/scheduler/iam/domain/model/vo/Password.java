package com.hyuse98.scheduler.iam.domain.model.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public final class Password {

    private final String value;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 128;

    public Password() {
        this.value = null;
    }

    private Password(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        this.value = hashedPassword;
    }

    public static void validateRaw(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (rawPassword.length() < PASSWORD_MIN_LENGTH || rawPassword.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("Password length must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters");
        }
        if (!rawPassword.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password should contain at least 1 uppercase character");
        }
        if (!rawPassword.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password should contain at least 1 number character");
        }
    }

    public static Password fromHashed(String hashedPassword) {
        return new Password(hashedPassword);
    }

    public String getHashedPassword() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{hashedValue='[PROTECTED]'}";
    }
}