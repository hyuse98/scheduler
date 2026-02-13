package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.usecases.client.impl.DisableClientUseCaseImpl;
import com.hyuse98.scheduler.iam.UserStatusEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserDisableListener {

    private final DisableClientUseCaseImpl disableClientUseCaseImpl;

    public UserDisableListener(DisableClientUseCaseImpl disableClientUseCaseImpl) {
        this.disableClientUseCaseImpl = disableClientUseCaseImpl;
    }

    @RabbitListener(queues = "core.user.active.queue")
    public void onUserDisable(UserStatusEvent event) {

        disableClientUseCaseImpl.execute(event.id());
    }
}
