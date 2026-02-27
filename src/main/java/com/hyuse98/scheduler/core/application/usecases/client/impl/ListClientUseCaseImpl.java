package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.application.exceptions.ClientCollectionEmpty;
import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListClientUseCaseImpl implements ListClientUseCase {

    private final ClientRepository clientRepository;

    public ListClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional()
    public List<Client> execute() {

        List<Client> list = clientRepository.findAll().stream().toList();

        if (list.isEmpty()) {
            throw new ClientCollectionEmpty("List is empty");
        }

        return list;
    }
}
