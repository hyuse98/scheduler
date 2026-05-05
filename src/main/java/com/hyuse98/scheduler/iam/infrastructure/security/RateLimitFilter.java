package com.hyuse98.scheduler.iam.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiting filter that enforces per-user (authenticated) and per-IP (public) request limits.
 * <p>
 * NOT declared as {@code @Component} intentionally — registered only within the Spring Security
 * filter chain via {@link com.hyuse98.scheduler.iam.infrastructure.config.SecurityConfig}
 * to avoid Spring Boot's automatic servlet-level registration (double registration problem).
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    private static final String HEADER_LIMIT       = "X-RateLimit-Limit";
    private static final String HEADER_REMAINING   = "X-RateLimit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-RateLimit-Retry-After";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String s && s.equals("anonymousUser"));

        String bucketKey = isAuthenticated
                ? "rate:user:" + authentication.getName()
                : "rate:ip:" + getClientIp(request);

        try {
            Bucket bucket = rateLimitService.resolveBucket(bucketKey, isAuthenticated);
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

            if (probe.isConsumed()) {
                long remaining = probe.getRemainingTokens();
                response.addHeader(HEADER_LIMIT, String.valueOf(remaining + 1));
                response.addHeader(HEADER_REMAINING, String.valueOf(remaining));
                filterChain.doFilter(request, response);
            } else {
                long retryAfterSeconds = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()) + 1;
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.addHeader(HEADER_LIMIT, "0");
                response.addHeader(HEADER_REMAINING, "0");
                response.addHeader(HEADER_RETRY_AFTER, String.valueOf(retryAfterSeconds));

                Map<String, Object> body = Map.of(
                        "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                        "error", "Too Many Requests",
                        "message", "Rate limit exceeded. Try again in " + retryAfterSeconds + " second(s).",
                        "retryAfter", retryAfterSeconds
                );
                response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
            }
        } catch (Exception e) {
            // Fail-open: if Redis is unavailable or config is invalid, allow the request through
            log.warn("Rate limiting unavailable for key '{}', failing open. Cause: {}", bucketKey, e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
