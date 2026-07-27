package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.ClientRegisteredEvent;
import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientAlreadyExistException;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ClientEntityMapper clientEntityMapper;

    @InjectMocks
    private SaveClientUseCaseImpl saveClientUseCase;

    private Client buildClient(UUID id, String email, String name) {
        return Client.create(id, email, name, "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), true);
    }

    @Test
    void shouldSaveClientSuccessfully() {
        UUID id = UUID.randomUUID();
        CreateClientRequest request = new CreateClientRequest(id, "john@test.com", "John Doe",
                "11999999999", new Date(), "Rua A, 123", "123456789012345", new Date(), true);

        Client mappedClient = buildClient(id, "john@test.com", "John Doe");
        Client savedClient = buildClient(id, "john@test.com", "John Doe");

        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(clientEntityMapper.toDomain(request)).thenReturn(mappedClient);
        when(clientRepository.save(mappedClient)).thenReturn(savedClient);

        assertDoesNotThrow(() -> saveClientUseCase.execute(request));

        verify(clientRepository).findByEmail("john@test.com");
        verify(clientEntityMapper).toDomain(request);
        verify(clientRepository).save(mappedClient);
    }

    @Test
    void shouldPublishClientRegisteredEventAfterSave() {
        UUID id = UUID.randomUUID();
        CreateClientRequest request = new CreateClientRequest(id, "john@test.com", "John Doe",
                "11999999999", new Date(), "Rua A, 123", "123456789012345", new Date(), true);

        Client mappedClient = buildClient(id, "john@test.com", "John Doe");
        Client savedClient = buildClient(id, "john@test.com", "John Doe");

        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(clientEntityMapper.toDomain(request)).thenReturn(mappedClient);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        saveClientUseCase.execute(request);

        ArgumentCaptor<ClientRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(ClientRegisteredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ClientRegisteredEvent event = eventCaptor.getValue();
        assertEquals(savedClient.getId(), event.clientId());
        assertEquals(savedClient.getEmail(), event.clientEmail());
        assertEquals(savedClient.getName(), event.clientName());
    }

    @Test
    void shouldThrowClientAlreadyExistExceptionWhenEmailAlreadyRegistered() {
        UUID id = UUID.randomUUID();
        CreateClientRequest request = new CreateClientRequest(id, "existing@test.com", "John Doe",
                "11999999999", new Date(), "Rua A, 123", "123456789012345", new Date(), true);

        Client existingClient = buildClient(id, "existing@test.com", "Existing User");

        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.of(existingClient));

        assertThrows(ClientAlreadyExistException.class, () -> saveClientUseCase.execute(request));

        verify(clientRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
