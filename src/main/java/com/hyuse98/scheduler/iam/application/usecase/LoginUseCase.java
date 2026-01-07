package com.hyuse98.scheduler.iam.application.usecase;

import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;

public interface LoginUseCase {
    JwtResponse execute(LoginRequest request);
}
