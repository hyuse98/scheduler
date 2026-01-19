package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.ClientRequestEvent;
import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.context.ApplicationEventPublisher;

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
    public void execute(ClientDto clientDto) {

        Client newClient = clientEntityMapper.toDomain(clientDto);

        clientRepository.save(newClient);

        var event = new ClientRequestEvent(
                //Todo(Create event template do publish)
        );

        eventPublisher.publishEvent(event);
    }
}
