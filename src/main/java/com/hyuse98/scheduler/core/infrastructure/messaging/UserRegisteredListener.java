package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
import com.hyuse98.scheduler.iam.UserRegisteredEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserRegisteredListener {

    private final SaveClientUseCase saveClientUseCase;

    public UserRegisteredListener(SaveClientUseCase saveClientUseCase) {
        this.saveClientUseCase = saveClientUseCase;
    }

    @ApplicationModuleListener
    public void onUserRegistered(UserRegisteredEvent event) {

        ClientDto registrationDto = new ClientDto(
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
