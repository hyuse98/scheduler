package com.hyuse98.scheduler.iam.domain.model.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void shouldValidateRawPasswordSuccessfully() {
        assertDoesNotThrow(() -> Password.validateRaw("Valid1Password"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "short1A", "NoNumberHere", "nouppercase1"})
    void shouldThrowExceptionForInvalidRawPassword(String invalidPassword) {
        assertThrows(IllegalArgumentException.class, () -> {
            Password.validateRaw(invalidPassword);
        });
    }
    
    @Test
    void shouldThrowExceptionForNullRawPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            Password.validateRaw(null);
        });
    }

    @Test
    void shouldCreatePasswordFromHashed() {
        String hashedPassword = "hashed_password_value";
        Password password = Password.fromHashed(hashedPassword);
        assertNotNull(password);
        assertEquals(hashedPassword, password.getHashedPassword());
    }

    @Test
    void shouldThrowExceptionWhenCreatingFromNullHashedPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            Password.fromHashed(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenCreatingFromEmptyHashedPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            Password.fromHashed("");
        });
    }

    @Test
    void shouldBeEqualForSameHashedValue() {
        Password p1 = Password.fromHashed("hash1");
        Password p2 = Password.fromHashed("hash1");
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentHashedValue() {
        Password p1 = Password.fromHashed("hash1");
        Password p2 = Password.fromHashed("hash2");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }
}
