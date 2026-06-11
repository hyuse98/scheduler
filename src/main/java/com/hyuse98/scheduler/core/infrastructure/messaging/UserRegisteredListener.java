package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.messaging.dto.UserRegisteredMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserRegisteredListener {

    private final SaveClientUseCase saveClientUseCase;

    public UserRegisteredListener(SaveClientUseCase saveClientUseCase) {
        this.saveClientUseCase = saveClientUseCase;
    }

    @RabbitListener(queues = "core.user.registered.queue")
    public void onUserRegistered(UserRegisteredMessage event) {

        CreateClientRequest registrationDto = new CreateClientRequest(
                event.userId(),
                event.userEmail(),
                "PENDENTE",
                "PENDENTE",
                new Date(),
                "PENDENTE",
                "PENDENTE",
                new Date(),
                true
        );

        saveClientUseCase.execute(registrationDto);
    }
}
