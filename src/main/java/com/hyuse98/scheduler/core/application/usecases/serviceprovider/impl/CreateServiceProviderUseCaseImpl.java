package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderAlreadyExistException;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.CreateServiceProviderUseCase;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateServiceProviderUseCaseImpl implements CreateServiceProviderUseCase {

    private final ServiceProviderRepository repository;

    public CreateServiceProviderUseCaseImpl(ServiceProviderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ServiceProvider execute(UUID id, String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new ServiceProviderAlreadyExistException("Service Provider with email " + email + " already exists.");
        }

        ServiceProvider newProvider = ServiceProvider.create(
                id,
                "PENDENTE",
                email,
                "PENDENTE",
                null,
                "PENDENTE",
                "PENDENTE",
                "PENDENTE",
                new Date(),
                true
        );

        return repository.save(newProvider);
    }
}