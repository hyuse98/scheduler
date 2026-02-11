package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.client.EnableClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class EnableClientUseCaseImpl implements EnableClientUseCase {

    private final ClientRepository clientRepository;

    public EnableClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " not found"));

        clientRepository.save(client.enable());
    }
}
