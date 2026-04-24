package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.usecases.serviceprovider.DeleteServiceProviderUseCase;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteServiceProviderUseCaseImpl implements DeleteServiceProviderUseCase {

    private final ServiceProviderRepository serviceProviderRepository;

    public DeleteServiceProviderUseCaseImpl(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        serviceProviderRepository.deleteById(id);
    }
}
