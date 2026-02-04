package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.ClientRegisteredEvent;
import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public class SaveClientUseCaseImpl implements SaveClientUseCase {

    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ClientEntityMapper clientEntityMapper;
    private static final Logger LOG = LoggerFactory.getLogger(SaveClientUseCaseImpl.class);


    public SaveClientUseCaseImpl(ClientRepository clientRepository, ApplicationEventPublisher eventPublisher, ClientEntityMapper clientEntityMapper) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
        this.clientEntityMapper = clientEntityMapper;
    }

    @Override
    public void execute(CreateClientRequest createClientRequest) {

        if (clientRepository.existsById(createClientRequest.id())) {
            LOG.warn("Client with id {} already exists", createClientRequest.id());
            return;
        }

        if (clientRepository.findByEmail(createClientRequest.email()).isPresent()) {
            throw new EntityExistsException("Email already exists: " + createClientRequest.email());
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
