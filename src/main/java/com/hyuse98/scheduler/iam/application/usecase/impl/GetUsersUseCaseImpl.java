package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;

import java.util.List;

public class GetUsersUseCaseImpl implements GetUsersUsecase {

    private final UserRepository userRepository;

    public GetUsersUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}
