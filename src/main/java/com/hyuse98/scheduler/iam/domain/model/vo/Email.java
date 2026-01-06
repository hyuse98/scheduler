package com.hyuse98.scheduler.iam.domain.model.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public final class Email {

    private final String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Email() {
        this.value = null;
    }

    private Email(String email) {
        Objects.requireNonNull(email, "email cannot be null");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("email format is invalid");
        }
        this.value = email;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }
}