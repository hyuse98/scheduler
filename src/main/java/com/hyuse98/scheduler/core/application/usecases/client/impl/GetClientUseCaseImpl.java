package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetClientUseCaseImpl implements GetClientUseCase {

    private final ClientRepository clientRepository;

    public GetClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client execute(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with Id " + id + " not found"));
    }

    @Override
    @Transactional
    public Client execute(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client with Email " + email + " not found"));
    }
}
