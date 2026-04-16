package com.hyuse98.scheduler.iam.infrastructure.config;

import com.hyuse98.scheduler.iam.application.usecase.GetUserProfileUsecase;
import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import com.hyuse98.scheduler.iam.application.usecase.impl.GetUserProfileUseCaseImpl;
import com.hyuse98.scheduler.iam.application.usecase.impl.GetUsersUseCaseImpl;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UserDetailsService userDetailsService;

    public ApplicationConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("Error to configure Authentication Manager", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GetUsersUsecase getUsersUsecase(UserRepository userRepository) {
        return new GetUsersUseCaseImpl(userRepository);
    }

    @Bean
    public GetUserProfileUsecase getUserProfileUsecase(UserRepository userRepository) {
        return new GetUserProfileUseCaseImpl(userRepository);
    }
}
