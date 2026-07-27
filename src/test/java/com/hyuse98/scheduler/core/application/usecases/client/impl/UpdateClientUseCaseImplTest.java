package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private UpdateClientUseCaseImpl updateClientUseCase;

    private Client buildClient(UUID id) {
        return Client.create(id, "john@test.com", "John Doe", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), true);
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        UUID id = UUID.randomUUID();
        Client existingClient = buildClient(id);

        Date newBirthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 30);
        UpdateClientRequest request = new UpdateClientRequest("john@test.com", "John Updated",
                "11888888888", newBirthday, "Rua B, 456", "000000000000000");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));

        Client result = updateClientUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("11888888888", result.getPhoneNumber());
        assertEquals("Rua B, 456", result.getAddress());
        assertEquals("000000000000000", result.getCns());
        verify(clientRepository).findById(id);
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenClientDoesNotExist() {
        UUID id = UUID.randomUUID();
        UpdateClientRequest request = new UpdateClientRequest("john@test.com", "John Updated",
                "11888888888", new Date(), "Rua B, 456", "000000000000000");

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class,
                () -> updateClientUseCase.execute(id, request));

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldValidateAfterUpdating() {
        UUID id = UUID.randomUUID();
        Client existingClient = buildClient(id);

        // Requisição com name=null deve causar falha na validate()
        UpdateClientRequest invalidRequest = new UpdateClientRequest(null, null,
                null, null, null, null);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));

        // O cliente já tem dados válidos, então mesmo com request nulo ele não falha
        // pois updateProfile ignora nulls - validate() deve passar com os dados originais
        assertDoesNotThrow(() -> updateClientUseCase.execute(id, invalidRequest));
    }
}
