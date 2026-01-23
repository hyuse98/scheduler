package com.hyuse98.scheduler.core.infrastructure.config;

import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCaseImpl;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientBeanConfig {

    @Bean
    public SaveClientUseCase saveClientUseCase(
            ClientRepository clientRepository,
            ApplicationEventPublisher eventPublisher,
            ClientEntityMapper clientEntityMapper) {

        return new SaveClientUseCaseImpl(
                clientRepository,
                eventPublisher,
                clientEntityMapper
        );
    }
}
