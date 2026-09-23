package com.hyuse98.scheduler.iam.infrastructure.security;

import com.hyuse98.scheduler.iam.infrastructure.config.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class RateLimitService {

    private final ProxyManager<String> proxyManager;
    private final RateLimitProperties properties;

    public RateLimitService(ProxyManager<String> proxyManager, RateLimitProperties properties) {
        this.proxyManager = proxyManager;
        this.properties = properties;
    }

    /**
     * Resolves or creates a Bucket for the given key in Redis.
     * Keys are stored as human-readable strings (e.g. "rate:user:email@example.com").
     *
     * @param key             logical Redis key
     * @param isAuthenticated whether to apply the authenticated or public rate limit profile
     * @return the Bucket associated with the key
     */
    public Bucket resolveBucket(String key, boolean isAuthenticated) {
        Supplier<BucketConfiguration> configSupplier = isAuthenticated
                ? () -> buildConfig(properties.authenticated())
                : () -> buildConfig(properties.pub());

        return proxyManager.builder().build(key, configSupplier);
    }

    private BucketConfiguration buildConfig(RateLimitProperties.Profile profile) {
        // refillIntervally: replenishes all tokens in one shot at the END of each interval.
        // This is deterministic — capacity: 10 with 1 min interval means exactly 10 req/min.
        // (refillGreedy would drip tokens continuously, allowing bursts across interval boundaries)
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(profile.capacity())
                .refillIntervally(profile.refillTokens(), Duration.ofMinutes(profile.refillDurationMinutes()))
                .build();

        return BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }

}
