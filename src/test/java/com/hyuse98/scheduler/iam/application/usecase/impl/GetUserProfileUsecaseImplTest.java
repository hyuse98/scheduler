package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.exceptions.UserNotFoundException;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserProfileUsecaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserProfileUsecaseImpl getUserProfileUsecase;

    @Test
    void shouldGetUserProfileSuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = User.reconstitute(userId, Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserProfileResponse response = getUserProfileUsecase.execute(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("test@example.com", response.email());
        assertEquals(user.getRoles().toString(), response.role());
        
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            getUserProfileUsecase.execute(userId);
        });

        verify(userRepository).findById(userId);
    }
}
