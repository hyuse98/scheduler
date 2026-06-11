package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.dto.UpdateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.UpdateServiceProviderUseCase;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateServiceProviderUseCaseImpl implements UpdateServiceProviderUseCase {

    private final ServiceProviderRepository repository;

    public UpdateServiceProviderUseCaseImpl(ServiceProviderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ServiceProvider execute(UUID id, UpdateServiceProviderRequest request) {
        ServiceProvider provider = repository.findById(id)
                .orElseThrow(() -> new ServiceProviderNotFoundException("Service Provider not found"));

        provider.updateProfile(
                request.name(),
                request.phoneNumber(),
                request.birthday(),
                request.address(),
                request.expertise(),
                request.registry()
        );

        provider.validate();
        return repository.save(provider);
    }
}