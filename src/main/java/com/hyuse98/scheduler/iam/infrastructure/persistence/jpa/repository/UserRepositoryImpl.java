package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RoleJpaEntity;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {

        UserJpaEntity userJpaEntity = userMapper.toEntity(user);

        Set<RoleJpaEntity> managedRoles = userJpaEntity.getRoles().stream()
                .map(role -> roleJpaRepository.findByName(role.getName())
                        .orElseThrow(() -> new IllegalStateException("Role not found: " + role.getName())))
                .collect(Collectors.toSet());

        userJpaEntity.setRoles(managedRoles);

        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}