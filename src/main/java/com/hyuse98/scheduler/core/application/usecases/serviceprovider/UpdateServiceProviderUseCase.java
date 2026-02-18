package com.hyuse98.scheduler.core.application.usecases.serviceprovider;

import com.hyuse98.scheduler.core.application.dto.UpdateServiceProviderRequest;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface UpdateServiceProviderUseCase {
    @Transactional
    ServiceProvider execute(UUID id, UpdateServiceProviderRequest request);
}
