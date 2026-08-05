package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GetUsersUsecase getUsersUsecase;

    @InjectMocks
    private AdminUserController adminUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
    }

    @Test
    void shouldGetUsers() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.reconstitute(userId, Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        
        when(getUsersUsecase.execute()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/admin/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].role").value("ROLE_USER"));

        verify(getUsersUsecase).execute();
    }

    //TODO Refactor
//    @Test
//    void shouldDisableUser() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        mockMvc.perform(post("/api/v1/admin/user/disable")
//                        .param("id", userId.toString()))
//                .andExpect(status().isAccepted());
//
//        verify(disableUserUsecase).execute(userId);
//    }

    //TODO Refactor
//    @Test
//    void shouldEnableUser() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        mockMvc.perform(post("/api/v1/admin/user/enable")
//                        .param("id", userId.toString()))
//                .andExpect(status().isAccepted());
//
//        verify(enableUserUsecase).execute(userId);
//    }
}
