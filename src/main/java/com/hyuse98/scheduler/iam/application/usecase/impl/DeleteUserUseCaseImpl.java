package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.usecase.DeleteUserUseCase;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        // Normally you might want to check if it exists or publish an event, 
        // but for a simple hard delete teardown we can just delete directly.
        userRepository.deleteById(id);
    }
}
