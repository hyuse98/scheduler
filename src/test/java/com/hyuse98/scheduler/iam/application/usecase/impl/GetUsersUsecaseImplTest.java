package com.hyuse98.scheduler.iam.application.usecase.impl;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersUsecaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUsersUseCaseImpl getUsersUsecase;

    @Test
    void shouldGetAllUsers() {
        User user1 = User.reconstitute(UUID.randomUUID(), Email.of("test1@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_USER), true);
        User user2 = User.reconstitute(UUID.randomUUID(), Email.of("test2@example.com"), Password.fromHashed("pass"), Set.of(Role.ROLE_ADMIN), true);
        
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = getUsersUsecase.execute();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }
}
