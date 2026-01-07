package com.hyuse98.scheduler.iam.application.usecase;

import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;

public interface RegisterUseCase {
    public void execute(RegistrationRequest request);
}
