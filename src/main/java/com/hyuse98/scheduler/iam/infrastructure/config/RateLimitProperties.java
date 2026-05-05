package com.hyuse98.scheduler.iam.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "rate-limit")
public record RateLimitProperties(
        Profile authenticated,
        Profile pub
) {

    public record Profile(
            int capacity,
            int refillTokens,
            int refillDurationMinutes
    ) {}
}
