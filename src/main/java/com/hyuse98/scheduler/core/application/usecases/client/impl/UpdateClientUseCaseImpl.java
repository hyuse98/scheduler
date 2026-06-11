package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.client.UpdateClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateClientUseCaseImpl implements UpdateClientUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client execute(UUID id, UpdateClientRequest request) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with ID " + id + " not found"));

        client.updateProfile(
                request.name(),
                request.phoneNumber(),
                request.birthday(),
                request.address(),
                request.cns()
        );

        client.validate();

        return client;
    }
}
