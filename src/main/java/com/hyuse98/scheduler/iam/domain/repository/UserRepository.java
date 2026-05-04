package com.hyuse98.scheduler.iam.domain.repository;


import com.hyuse98.scheduler.iam.domain.model.aggregate.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String username);
}
