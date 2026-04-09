package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.usecases.serviceprovider.CreateServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.messaging.dto.ServiceProviderRegisteredMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderRegisteredListener {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceProviderRegisteredListener.class);

    private final CreateServiceProviderUseCase createServiceProviderUseCase;

    public ServiceProviderRegisteredListener(CreateServiceProviderUseCase createServiceProviderUseCase) {
        this.createServiceProviderUseCase = createServiceProviderUseCase;
    }

    @RabbitListener(queues = "core.provider.registered.queue")
    public void onServiceProviderRegistered(ServiceProviderRegisteredMessage message) {
        LOG.info("Recebido evento de registro de prestador de serviços: {}", message.userEmail());
        createServiceProviderUseCase.execute(message.userId(), message.userEmail());
    }
}
