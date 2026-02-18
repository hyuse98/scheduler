package com.hyuse98.scheduler.core.application.usecases.serviceprovider;

import com.hyuse98.scheduler.core.application.dto.CreateServiceProviderRequest;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import jakarta.transaction.Transactional;

public interface CreateServiceProviderUseCase {

    @Transactional
    ServiceProvider execute(CreateServiceProviderRequest request);
}
