package com.hyuse98.scheduler.iam.domain.model.aggregate;

import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Email email;
    private Password password;
    private Set<Role> initialRoles;

    @BeforeEach
    void setUp() {
        email = Email.of("test@example.com");
        password = Password.fromHashed("hashed_password");
        initialRoles = Set.of(Role.ROLE_USER);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = User.create(email, password, initialRoles);

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password.getHashedPassword(), user.getPassword());
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        assertTrue(user.isEnabled());
        assertEquals(email.getValue(), user.getUsername());
    }

    @Test
    void shouldReconstituteUserSuccessfully() {
        UUID id = UUID.randomUUID();
        User user = User.reconstitute(id, email, password, initialRoles, false);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password.getHashedPassword(), user.getPassword());
        assertEquals(initialRoles, user.getRoles());
        assertFalse(user.isEnabled());
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithNullEmail() {
        assertThrows(NullPointerException.class, () -> {
            User.create(null, password, initialRoles);
        });
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithNullPassword() {
        assertThrows(NullPointerException.class, () -> {
            User.create(email, null, initialRoles);
        });
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithNullRoles() {
        assertThrows(NullPointerException.class, () -> {
            User.create(email, password, null);
        });
    }

    @Test
    void shouldChangePassword() {
        User user = User.create(email, password, initialRoles);
        Password newPassword = Password.fromHashed("new_hashed_password");
        
        user.changePassword(newPassword);
        
        assertEquals("new_hashed_password", user.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenChangingToNullPassword() {
        User user = User.create(email, password, initialRoles);
        assertThrows(NullPointerException.class, () -> {
            user.changePassword(null);
        });
    }

    @Test
    void shouldAddRole() {
        User user = User.create(email, password, initialRoles);
        user.addRole(Role.ROLE_ADMIN);
        
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullRole() {
        User user = User.create(email, password, initialRoles);
        assertThrows(NullPointerException.class, () -> {
            user.addRole(null);
        });
    }

    @Test
    void shouldRemoveRole() {
        User user = User.create(email, password, Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
        user.removeRole(Role.ROLE_ADMIN);
        
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        assertFalse(user.getRoles().contains(Role.ROLE_ADMIN));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNullRole() {
        User user = User.create(email, password, initialRoles);
        assertThrows(NullPointerException.class, () -> {
            user.removeRole(null);
        });
    }

    @Test
    void shouldDisableAndEnableUser() {
        User user = User.create(email, password, initialRoles);
        assertTrue(user.isEnabled());
        
        user.disable();
        assertFalse(user.isEnabled());
        
        user.enable();
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        User user = User.create(email, password, Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}
