package com.hyuse98.scheduler.iam.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // A 256-bit secret key (32 bytes) base64 encoded for testing
        String testSecretKey = "bXktc2VjcmV0LWtleS1mb3ItdGVzdGluZy1wdXJwb3Nlcy1vbmx5LXRvLXNhZmV0eS1jaGVjaw==";
        ReflectionTestUtils.setField(tokenService, "secretKey", testSecretKey);
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", 1000 * 60 * 60); // 1 hour
        
        userDetails = User.withUsername("test@example.com")
                          .password("password")
                          .authorities(Collections.emptyList())
                          .build();
    }

    @Test
    void shouldGenerateAndExtractUsername() {
        String token = tokenService.generateToken(userDetails);
        
        assertNotNull(token);
        
        String extractedUsername = tokenService.extractUsername(token);
        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = tokenService.generateToken(userDetails);
        
        assertTrue(tokenService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldReturnFalseForDifferentUser() {
        String token = tokenService.generateToken(userDetails);
        
        UserDetails otherUser = User.withUsername("other@example.com")
                                    .password("password")
                                    .authorities(Collections.emptyList())
                                    .build();
                                    
        assertFalse(tokenService.isTokenValid(token, otherUser));
    }
}
