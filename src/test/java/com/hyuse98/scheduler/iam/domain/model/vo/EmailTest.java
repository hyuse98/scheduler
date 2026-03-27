package com.hyuse98.scheduler.iam.domain.model.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmailWithValidFormat() {
        String validEmailStr = "test@example.com";
        Email email = Email.of(validEmailStr);
        assertNotNull(email);
        assertEquals(validEmailStr, email.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "test@.com", "@example.com", "test@example"})
    void shouldThrowExceptionForInvalidEmailFormat(String invalidEmail) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Email.of(invalidEmail);
        });
        assertEquals("email format is invalid", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Email.of(null);
        });
        assertEquals("email cannot be null", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameEmailString() {
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentEmailString() {
        Email email1 = Email.of("test1@example.com");
        Email email2 = Email.of("test2@example.com");
        
        assertNotEquals(email1, email2);
        assertNotEquals(email1.hashCode(), email2.hashCode());
    }
}
