package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.UserStatusEvent;
import com.hyuse98.scheduler.iam.application.exceptions.UserNotFoundException;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisableUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DisableUserUseCaseImpl disableUserUseCase;

    @Test
    void shouldDisableUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        User enabledUser = User.reconstitute(userId, Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(enabledUser));
        when(userRepository.save(any(User.class))).thenReturn(enabledUser);

        disableUserUseCase.execute(userId);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertFalse(userCaptor.getValue().isEnabled());

        ArgumentCaptor<UserStatusEvent> eventCaptor = ArgumentCaptor.forClass(UserStatusEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(userId, eventCaptor.getValue().id());
        assertFalse(eventCaptor.getValue().isActive());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            disableUserUseCase.execute(userId);
        });

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
