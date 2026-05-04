package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@RedisHash("refresh_token")
public class RefreshToken {
    @Id
    private String token;

    private UUID userId;

    @TimeToLive
    private Long expirationTime; // TTL in seconds

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }
}