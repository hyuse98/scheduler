package com.hyuse98.scheduler.iam.infrastructure.config;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RefreshToken;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper.UserMapper;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs; // Ex: 7 dias em milissegundos

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, UserMapper userMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken refreshToken = new RefreshToken();

        User user = userRepository.findById(userId).orElseThrow();

        refreshToken.setUser(userMapper.toEntity(user));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. login again.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken);
    }
}
