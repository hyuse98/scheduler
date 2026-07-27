package com.hyuse98.scheduler.iam.domain.repository;


import com.hyuse98.scheduler.iam.domain.model.aggregate.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String username);

    Optional<User> findById(UUID id);

    List<User> findAll();
}
