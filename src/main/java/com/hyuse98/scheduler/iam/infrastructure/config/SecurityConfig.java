package com.hyuse98.scheduler.iam.infrastructure.config;

import com.hyuse98.scheduler.iam.infrastructure.security.JwtAuthFilter;
import com.hyuse98.scheduler.iam.infrastructure.security.RateLimitFilter;
import com.hyuse98.scheduler.iam.infrastructure.security.RateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final RateLimitService rateLimitService;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(
            JwtAuthFilter jwtFilter,
            RateLimitService rateLimitService,
            AuthenticationProvider authenticationProvider
    ) {
        this.jwtFilter = jwtFilter;
        this.rateLimitService = rateLimitService;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * RateLimitFilter is created as a @Bean here (not @Component) to ensure it is
     * registered ONLY inside the Spring Security filter chain — preventing Spring Boot's
     * automatic servlet-level registration which would cause double execution and
     * interfere with the security context.
     */
    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter(rateLimitService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api/v1/iam/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter(), JwtAuthFilter.class);

        return http.build();
    }
}