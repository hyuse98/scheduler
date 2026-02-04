package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class UpdateClientUseCaseImpl implements UpdateClientUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void execute(UUID id, UpdateClientRequest request) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("{}"));

        client.updateProfile(
                request.name(),
                request.phoneNumber(),
                request.birthday(),
                request.address(),
                request.cns()
        );

        clientRepository.save(client);
    }
}
