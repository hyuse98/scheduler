package com.hyuse98.scheduler.core.infrastructure.messaging;

import com.hyuse98.scheduler.core.application.usecases.client.impl.EnableClientUseCaseImpl;
import com.hyuse98.scheduler.iam.UserStatusEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEnableListener {

    private final EnableClientUseCaseImpl enableClientUseCaseImpl;

    public UserEnableListener(EnableClientUseCaseImpl enableClientUseCaseImpl) {
        this.enableClientUseCaseImpl = enableClientUseCaseImpl;
    }

    @RabbitListener(queues = "core.user.active.queue")
    public void onUserEnable(UserStatusEvent event) {

        enableClientUseCaseImpl.execute(event.id());
    }
}
