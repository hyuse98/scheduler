package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;
import com.hyuse98.scheduler.iam.infrastructure.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginUseCaseImpl loginUsecase;

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(tokenService.generateToken(userDetails)).thenReturn("dummy_token");

        JwtResponse response = loginUsecase.execute(request);

        assertNotNull(response);
        assertEquals("dummy_token", response.token());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(userDetails);
    }
}
