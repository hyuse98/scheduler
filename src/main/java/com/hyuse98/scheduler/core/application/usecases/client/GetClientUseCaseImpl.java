package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;

import java.util.Optional;
import java.util.UUID;

public class GetClientUseCaseImpl implements GetClientUseCase {

    private final ClientRepository clientRepository;

    public GetClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> execute(UUID id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> execute(String email) {
        return clientRepository.findByEmail(email);
    }
}
