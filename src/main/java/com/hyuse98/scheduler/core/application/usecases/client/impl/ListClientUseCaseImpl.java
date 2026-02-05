package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class ListClientUseCaseImpl implements ListClientUseCase {

    private final ClientRepository clientRepository;

    public ListClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional()
    public List<Client> execute() {
        return (List<Client>) clientRepository.findAll();
    }
}
