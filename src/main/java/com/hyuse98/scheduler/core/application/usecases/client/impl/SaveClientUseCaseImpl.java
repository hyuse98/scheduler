package com.hyuse98.scheduler.core.application.usecases.client.impl;

import com.hyuse98.scheduler.core.ClientRegisteredEvent;
import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientAlreadyExistException;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SaveClientUseCaseImpl implements SaveClientUseCase {

    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ClientEntityMapper clientEntityMapper;

    public SaveClientUseCaseImpl(ClientRepository clientRepository, ApplicationEventPublisher eventPublisher, ClientEntityMapper clientEntityMapper) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
        this.clientEntityMapper = clientEntityMapper;
    }

    @Override
    @Transactional
    public void execute(CreateClientRequest createClientRequest){

        if (clientRepository.findByEmail(createClientRequest.email()).isPresent()) {
            throw new ClientAlreadyExistException("Client with Email "+ createClientRequest.email() +" Already Exists: ");
        }

        Client newClient = clientEntityMapper.toDomain(createClientRequest);

        Client savedClient = clientRepository.save(newClient);

        eventPublisher.publishEvent(new ClientRegisteredEvent(
                savedClient.getId(),
                savedClient.getEmail(),
                savedClient.getName(),
                savedClient.getPhoneNumber(),
                savedClient.getBirthday(),
                savedClient.getAddress(),
                savedClient.getCns(),
                savedClient.getCreatedAt(),
                savedClient.getActive()
        ));
    }
}
