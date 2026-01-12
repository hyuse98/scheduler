package com.hyuse98.scheduler.iam.infrastructure.security;

import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .map(user -> {
                    String encodedPassword = user.getPassword();

                    return User
                            .withUsername(user.getUsername())
                            .password(encodedPassword)
                            .authorities("ROLE_USER")
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

}
