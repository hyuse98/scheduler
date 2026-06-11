package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.UserStatusEvent;
import com.hyuse98.scheduler.iam.application.exceptions.UserNotFoundException;
import com.hyuse98.scheduler.iam.application.usecase.EnableUserUsecase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EnableUserUseCaseImpl implements EnableUserUsecase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EnableUserUseCaseImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Client with id: " + id + " not found"));

        User savedUser = userRepository.save(user.enable());

        eventPublisher.publishEvent(new UserStatusEvent(
                savedUser.getId(),
                savedUser.isEnabled()
        ));
    }
}
