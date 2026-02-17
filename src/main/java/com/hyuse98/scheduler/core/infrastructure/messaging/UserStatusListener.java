package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.usecases.client.DisableClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.EnableClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.messaging.dto.UserStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserStatusListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserStatusListener.class);

    private final EnableClientUseCase enableClientUseCase;
    private final DisableClientUseCase disableClientUseCase;

    public UserStatusListener(EnableClientUseCase enableClientUseCase, DisableClientUseCase disableClientUseCase) {
        this.enableClientUseCase = enableClientUseCase;
        this.disableClientUseCase = disableClientUseCase;
    }

    @RabbitListener(queues = "core.user.active.queue")
    public void onUserStatusChanged(UserStatusMessage message) {
        LOG.info("Recebido evento de mudança de status para o cliente {}: isActive={}", message.id(), message.isActive());

        if (message.isActive()) {
            enableClientUseCase.execute(message.id());
        } else {
            disableClientUseCase.execute(message.id());
        }
    }
}