package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;
import com.hyuse98.scheduler.iam.application.usecase.LoginUseCase;
import com.hyuse98.scheduler.iam.infrastructure.security.TokenService;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.infrastructure.config.RefreshTokenService;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RefreshToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public LoginUseCaseImpl(AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public JwtResponse execute(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        assert user != null;
        String token = tokenService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new JwtResponse(token, refreshToken.getToken());
    }
}
