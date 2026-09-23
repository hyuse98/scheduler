package com.hyuse98.scheduler.iam.infrastructure.config;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RefreshToken;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken refreshToken = new RefreshToken();

        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        refreshToken.setUserId(userId);
        refreshToken.setExpirationTime(refreshTokenDurationMs / 1000); // TTL in seconds
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        // Since Redis handles expiration automatically via TTL, 
        // if the token is fetched successfully, it is considered valid.
        return token;
    }

    public Optional<RefreshToken> findByToken(String requestRefreshToken) {
        return refreshTokenRepository.findById(requestRefreshToken);
    }

    public User getUserFromToken(RefreshToken token) {
        return userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for refresh token"));
    }
}
