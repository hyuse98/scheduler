package com.hyuse98.scheduler.iam.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.security.Principal;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserProfileController userProfileController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }

    @Test
    void shouldGetProfile() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        mockMvc.perform(get("/api/v1/auth/client/me").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().string("Autenticado como: test@example.com"));
    }

    @Test
    void shouldChangePassword() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        mockMvc.perform(post("/api/v1/auth/client/me/change-password")
                        .principal(principal)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("newPassword123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Solicitação de troca de senha recebida para: test@example.com"));
    }
}
