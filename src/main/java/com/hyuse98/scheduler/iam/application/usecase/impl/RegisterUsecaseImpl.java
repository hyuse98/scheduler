package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.UserRegisteredEvent;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.application.usecase.RegisterUseCase;
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
public class RegisterUsecaseImpl implements RegisterUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RegisterUsecaseImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
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

        Email email = Email.of(request.email());
        Password password = Password.of(request.password(), passwordEncoder);
        Set<Role> initialRoles = Set.of(Role.ROLE_USER);

        User newUser = User.create(email, password, initialRoles);

        User savedUser = userRepository.save(newUser);

        eventPublisher.publishEvent(new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getEmail().getValue()
        ));
    }
}
