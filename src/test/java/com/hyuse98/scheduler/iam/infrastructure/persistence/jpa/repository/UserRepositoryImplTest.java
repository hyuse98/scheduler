package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RoleJpaEntity;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private RoleJpaRepository roleJpaRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void shouldSaveUser() {
        User user = User.reconstitute(UUID.randomUUID(), Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        
        UserJpaEntity jpaEntity = new UserJpaEntity();
        RoleJpaEntity roleJpaEntity = new RoleJpaEntity();
        roleJpaEntity.setName(RoleJpaEntity.RoleName.ROLE_USER);
        jpaEntity.setRoles(Set.of(roleJpaEntity));

        when(userMapper.toEntity(user)).thenReturn(jpaEntity);
        when(roleJpaRepository.findByName(RoleJpaEntity.RoleName.ROLE_USER)).thenReturn(Optional.of(roleJpaEntity));
        when(userJpaRepository.save(any(UserJpaEntity.class))).thenReturn(jpaEntity);
        when(userMapper.toDomain(jpaEntity)).thenReturn(user);

        User savedUser = userRepositoryImpl.save(user);

        assertNotNull(savedUser);
        verify(userJpaRepository).save(any(UserJpaEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFoundOnSave() {
        User user = User.reconstitute(UUID.randomUUID(), Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        
        UserJpaEntity jpaEntity = new UserJpaEntity();
        RoleJpaEntity roleJpaEntity = new RoleJpaEntity();
        roleJpaEntity.setName(RoleJpaEntity.RoleName.ROLE_USER);
        jpaEntity.setRoles(Set.of(roleJpaEntity));

        when(userMapper.toEntity(user)).thenReturn(jpaEntity);
        when(roleJpaRepository.findByName(RoleJpaEntity.RoleName.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            userRepositoryImpl.save(user);
        });
    }

    @Test
    void shouldFindByEmail() {
        String email = "test@example.com";
        UserJpaEntity jpaEntity = new UserJpaEntity();
        User user = User.reconstitute(UUID.randomUUID(), Email.of(email), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);

        when(userJpaRepository.findByEmail(email)).thenReturn(Optional.of(jpaEntity));
        when(userMapper.toDomain(jpaEntity)).thenReturn(user);

        Optional<User> foundUser = userRepositoryImpl.findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail().getValue());
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        UserJpaEntity jpaEntity = new UserJpaEntity();
        User user = User.reconstitute(id, Email.of("test@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));
        when(userMapper.toDomain(jpaEntity)).thenReturn(user);

        Optional<User> foundUser = userRepositoryImpl.findById(id);

        assertTrue(foundUser.isPresent());
        assertEquals(id, foundUser.get().getId());
    }
}
