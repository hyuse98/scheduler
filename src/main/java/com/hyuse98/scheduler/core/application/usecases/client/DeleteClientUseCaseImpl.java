package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
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
    public void execute(UUID id) {

        if (!clientRepository.existsById(id)) {
            LOG.warn("Client with id {} does not exist", id);
        }

        clientRepository.deleteById(id);
    }
}
