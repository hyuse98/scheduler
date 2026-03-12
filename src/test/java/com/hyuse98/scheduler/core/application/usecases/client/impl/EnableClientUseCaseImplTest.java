package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnableClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private EnableClientUseCaseImpl enableClientUseCase;

    private Client buildClient(UUID id, boolean active) {
        return Client.create(id, "john@test.com", "John Doe", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), active);
    }

    @Test
    void shouldEnableClientSuccessfully() {
        UUID id = UUID.randomUUID();
        Client disabledClient = buildClient(id, false);

        when(clientRepository.findById(id)).thenReturn(Optional.of(disabledClient));
        when(clientRepository.save(any(Client.class))).thenReturn(disabledClient);

        enableClientUseCase.execute(id);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());

        assertTrue(clientCaptor.getValue().getActive());
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenClientDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class,
                () -> enableClientUseCase.execute(id));

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(clientRepository, never()).save(any());
    }
}
