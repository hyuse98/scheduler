package com.hyuse98.scheduler.core.infrastructure.config;

import com.hyuse98.scheduler.core.application.usecases.client.*;
import com.hyuse98.scheduler.core.application.usecases.client.impl.*;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientBeanConfig {

    @Bean
    public SaveClientUseCase saveClientUseCase(ClientRepository clientRepository, ApplicationEventPublisher eventPublisher, ClientEntityMapper clientEntityMapper) {
        return new SaveClientUseCaseImpl(clientRepository, eventPublisher, clientEntityMapper);
    }

    @Bean
    public GetClientUseCase getClientUseCase(ClientRepository clientRepository) {
        return new GetClientUseCaseImpl(clientRepository);
    }

    @Bean
    public ListClientUseCase listClientUseCase(ClientRepository clientRepository) {
        return new ListClientUseCaseImpl(clientRepository);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepository clientRepository) {
        return new UpdateClientUseCaseImpl(clientRepository);
    }

    @Bean
    public EnableClientUseCase enableClientUseCase(ClientRepository clientRepository) {
        return new EnableClientUseCaseImpl(clientRepository);
    }

    @Bean
    public DisableClientUseCase disableClientUseCase(ClientRepository clientRepository) {
        return new DisableClientUseCaseImpl(clientRepository);
    }
}
