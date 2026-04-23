package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.usecases.client.DeleteClientUseCase;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteClientUseCaseImpl implements DeleteClientUseCase {

    private final ClientRepository clientRepository;

    public DeleteClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        clientRepository.deleteById(id);
    }
}
