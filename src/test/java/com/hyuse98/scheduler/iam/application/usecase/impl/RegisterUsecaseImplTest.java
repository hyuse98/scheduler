package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.UserRegisteredEvent;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUsecaseImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RegisterUsecaseImpl registerUsecase;

    private RegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RegistrationRequest("test@example.com", "Valid1Password");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRequest.password())).thenReturn("encoded_password");
        
        UUID generatedId = UUID.randomUUID();
        User savedUser = User.reconstitute(generatedId, Email.of(validRequest.email()), Password.fromHashed("encoded_password"), Set.of(Role.ROLE_USER), true);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        registerUsecase.execute(validRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        assertEquals(validRequest.email(), capturedUser.getEmail().getValue());
        assertEquals("encoded_password", capturedUser.getPassword());
        assertTrue(capturedUser.getRoles().contains(Role.ROLE_USER));

        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        UserRegisteredEvent capturedEvent = eventCaptor.getValue();
        
        assertEquals(generatedId, capturedEvent.userId());
        assertEquals(validRequest.email(), capturedEvent.userEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.of(User.create(Email.of(validRequest.email()), Password.fromHashed("pass"), Set.of(Role.ROLE_USER))));

        assertThrows(EntityExistsException.class, () -> {
            registerUsecase.execute(validRequest);
        });

        verify(userRepository, never()).save(any(User.class));
        verify(eventPublisher, never()).publishEvent(any());
    }
}
