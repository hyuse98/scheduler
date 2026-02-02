package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;

import java.util.List;

public class ListClientUseCaseImpl implements ListClientUseCase {

    private final ClientRepository clientRepository;

    public ListClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> execute() {
        return (List<Client>) clientRepository.findAll();
    }
}
