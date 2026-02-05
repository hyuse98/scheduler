package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.usecases.client.DeleteClientUseCase;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DeleteClientUseCaseImpl implements DeleteClientUseCase {

    private final ClientRepository clientRepository;
    private static final Logger LOG = LoggerFactory.getLogger(DeleteClientUseCaseImpl.class);

    public DeleteClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {

        if (!clientRepository.existsById(id)) {
            LOG.warn("Client with id {} does not exist", id);
            throw new EntityNotFoundException("Client with id " + id + " does not exist");
        }
        clientRepository.deleteById(id);
    }
}
