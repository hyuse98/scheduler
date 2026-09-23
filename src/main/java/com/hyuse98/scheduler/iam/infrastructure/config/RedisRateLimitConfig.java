package com.hyuse98.scheduler.iam.infrastructure.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RedisRateLimitConfig {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    public RedisRateLimitConfig(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    /**
     * Creates a Lettuce connection with a mixed codec:
     * - Keys  → StringCodec  (human-readable in Redis: "rate:ip:127.0.0.1")
     * - Values → ByteArrayCodec (Bucket4j internal binary state)
     *
     * Reuses host/port from the LettuceConnectionFactory already configured
     * by spring.data.redis.*.
     */
    @Bean
    public StatefulRedisConnection<String, byte[]> bucket4jRedisConnection() {
        RedisStandaloneConfiguration standaloneConfig = lettuceConnectionFactory.getStandaloneConfiguration();
        String host = standaloneConfig.getHostName();
        int port = standaloneConfig.getPort();

        RedisClient client = RedisClient.create(
                RedisURI.builder()
                        .withHost(host)
                        .withPort(port)
                        .build()
        );

        return client.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    /**
     * ProxyManager<String> — keys are stored as readable strings in Redis.
     * Uses Bucket4jLettuce.casBasedBuilder(StatefulRedisConnection) which internally
     * calls connection.async() — the non-deprecated API path.
     */
    @Bean
    public ProxyManager<String> bucketProxyManager(StatefulRedisConnection<String, byte[]> bucket4jRedisConnection) {
        return Bucket4jLettuce.casBasedBuilder(bucket4jRedisConnection).build();
    }
}
