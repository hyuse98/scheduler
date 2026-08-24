package com.hyuse98.scheduler.iam.application.usecase;

import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;

public interface RegisterServiceProviderUseCase {
    UserProfileResponse execute(RegistrationRequest request);
}
