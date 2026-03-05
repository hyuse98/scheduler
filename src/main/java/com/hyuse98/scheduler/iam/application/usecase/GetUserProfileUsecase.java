package com.hyuse98.scheduler.iam.application.usecase;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;

import java.util.UUID;

public interface GetUserProfileUsecase {

        UserProfileResponse execute(UUID id);

}
