package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetServiceProviderUseCaseImpl implements GetServiceProviderUseCase {

    private final ServiceProviderRepository repository;

    public GetServiceProviderUseCaseImpl(ServiceProviderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ServiceProvider execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceProviderNotFoundException("Service Provider not found"));
    }

    @Transactional
    @Override
    public ServiceProvider execute(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ServiceProviderNotFoundException("Service Provider not found"));
    }
}