package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.exceptions.UserNotFoundException;
import com.hyuse98.scheduler.iam.application.usecase.GetUserProfileUsecase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;

import java.util.UUID;

public class GetUserProfileUsecaseImpl implements GetUserProfileUsecase {

    private final UserRepository userRepository;

    public GetUserProfileUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponse execute(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with Id " + id + " not found"));

        return new UserProfileResponse(user.getId(), user.getEmail().getValue(), user.getRoles().toString());
    }
}
