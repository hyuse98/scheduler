package com.hyuse98.scheduler.iam.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.usecase.LoginUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterServiceProviderUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterUseCase;
import com.hyuse98.scheduler.iam.infrastructure.config.RefreshTokenService;
import com.hyuse98.scheduler.iam.infrastructure.security.TokenService;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RegisterUseCase registerUseCase;

    @Mock
    private RegisterServiceProviderUseCase registerServiceProviderUseCase;

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        RegistrationRequest request = new RegistrationRequest("test@example.com", "Password123");
        UUID userId = UUID.randomUUID();
        UserProfileResponse profileResponse = new UserProfileResponse(userId, "test@example.com", "ROLE_USER");

        when(registerUseCase.execute(any(RegistrationRequest.class))).thenReturn(profileResponse);

        mockMvc.perform(post("/api/v1/iam/auth/register/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(registerUseCase).execute(any(RegistrationRequest.class));
    }

    @Test
    void shouldRegisterServiceProvider() throws Exception {
        RegistrationRequest request = new RegistrationRequest("provider@example.com", "Password123");
        UUID providerId = UUID.randomUUID();
        UserProfileResponse profileResponse = new UserProfileResponse(providerId, "provider@example.com", "ROLE_SERVICE_PROVIDER");

        when(registerServiceProviderUseCase.execute(any(RegistrationRequest.class))).thenReturn(profileResponse);

        mockMvc.perform(post("/api/v1/iam/auth/register/provider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(providerId.toString()))
                .andExpect(jsonPath("$.email").value("provider@example.com"));

        verify(registerServiceProviderUseCase).execute(any(RegistrationRequest.class));
    }

    @Test
    void shouldLoginUser() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "Password123");
        JwtResponse response = new JwtResponse("mock_token", "mock_refresh");

        when(loginUseCase.execute(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/iam/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock_token"));

        verify(loginUseCase).execute(any(LoginRequest.class));
    }
}
