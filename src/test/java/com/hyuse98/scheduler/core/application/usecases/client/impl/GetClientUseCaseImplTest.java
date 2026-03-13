package com.hyuse98.scheduler.core.application.usecases.client.impl;

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
class GetClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private GetClientUseCaseImpl getClientUseCase;

    private Client buildClient(UUID id, String email) {
        return Client.create(id, email, "John Doe", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), true);
    }

    @Test
    void shouldGetClientByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        Client expected = buildClient(id, "john@test.com");

        when(clientRepository.findById(id)).thenReturn(Optional.of(expected));

        Client result = getClientUseCase.execute(id);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(clientRepository).findById(id);
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class,
                () -> getClientUseCase.execute(id));

        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    void shouldGetClientByEmailSuccessfully() {
        UUID id = UUID.randomUUID();
        String email = "john@test.com";
        Client expected = buildClient(id, email);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        Client result = getClientUseCase.execute(email);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(clientRepository).findByEmail(email);
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenEmailDoesNotExist() {
        String email = "notfound@test.com";

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class,
                () -> getClientUseCase.execute(email));

        assertTrue(ex.getMessage().contains(email));
    }
}
