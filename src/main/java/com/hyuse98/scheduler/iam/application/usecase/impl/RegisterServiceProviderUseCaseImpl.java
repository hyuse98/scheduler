package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.ServiceProviderRegisteredEvent;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.application.usecase.RegisterServiceProviderUseCase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RegisterServiceProviderUseCaseImpl implements RegisterServiceProviderUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RegisterServiceProviderUseCaseImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(RegistrationRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EntityExistsException("Email already exists: " + request.email());
        }

        Password.validateRaw(request.password());
        String hashedPassword = passwordEncoder.encode(request.password());

        Email email = Email.of(request.email());
        Password password = Password.fromHashed(hashedPassword);
        Set<Role> roles = Set.of(Role.ROLE_SERVICE_PROVIDER);

        User newUser = User.create(email, password, roles);
        User savedUser = userRepository.save(newUser);

        eventPublisher.publishEvent(new ServiceProviderRegisteredEvent(
                savedUser.getId(),
                savedUser.getEmail().getValue()
        ));
    }
}
