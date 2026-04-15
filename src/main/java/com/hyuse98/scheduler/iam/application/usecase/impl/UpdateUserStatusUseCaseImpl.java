package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.events.UserStatusEvent;
import com.hyuse98.scheduler.iam.application.exceptions.IllegalUserStateTransitionException;
import com.hyuse98.scheduler.iam.application.exceptions.UserNotFoundException;
import com.hyuse98.scheduler.iam.application.usecase.UpdateUserStatusUseCase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateUserStatusUseCaseImpl implements UpdateUserStatusUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UpdateUserStatusUseCaseImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(UUID userId, boolean newStatus) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Client with id: " + userId + " not found"));

        if (user.isEnabled() == newStatus) {
            String state = newStatus ? "active" : "deactivated";
            throw new IllegalUserStateTransitionException("User is already " + state);
        }

        User userToSave = newStatus ? user.enable() : user.disable();

        User savedUser = userRepository.save(userToSave);

        eventPublisher.publishEvent(new UserStatusEvent(
                savedUser.getId(),
                savedUser.isEnabled()
        ));
    }
}