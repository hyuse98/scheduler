package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.dto.CreateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderAlreadyExistException;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.CreateServiceProviderUseCase;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateServiceProviderUseCaseImpl implements CreateServiceProviderUseCase {

    private final ServiceProviderRepository repository;
    private final ServiceProviderEntityMapper mapper;

    public CreateServiceProviderUseCaseImpl(ServiceProviderRepository repository, ServiceProviderEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public ServiceProvider execute(CreateServiceProviderRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new ServiceProviderAlreadyExistException("Service Provider with Email " + request.email() + " Already Exists.");
        }

        ServiceProvider newProvider = mapper.toDomain(request);
        newProvider.validate();

        return repository.save(newProvider);
    }
}