package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.exceptions.ClientCollectionEmpty;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ListClientUseCaseImpl listClientUseCase;

    private Client buildClient(String email) {
        return Client.create(UUID.randomUUID(), email, "John Doe", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), true);
    }

    @Test
    void shouldReturnListOfClientsSuccessfully() {
        List<Client> clients = List.of(
                buildClient("alice@test.com"),
                buildClient("bob@test.com")
        );

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = listClientUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository).findAll();
    }

    @Test
    void shouldThrowClientCollectionEmptyWhenListIsEmpty() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ClientCollectionEmpty.class, () -> listClientUseCase.execute());
    }

    @Test
    void shouldReturnSingleClientList() {
        List<Client> clients = List.of(buildClient("single@test.com"));

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = listClientUseCase.execute();

        assertEquals(1, result.size());
    }
}
