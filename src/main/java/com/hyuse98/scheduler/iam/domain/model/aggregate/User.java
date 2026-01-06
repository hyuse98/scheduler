package com.hyuse98.scheduler.iam.domain.model.aggregate;

import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class User implements UserDetails {

    private UUID id;
    private Email email;
    private Password password;
    private Set<Role> roles;
    private boolean enabled;

    private User() {
    }

    private User(UUID id, Email email, Password password, Set<Role> roles, boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        Objects.requireNonNull(roles, "Roles set cannot be null");
    }

    public static User create(Email email, Password password, Set<Role> initialRoles) {
        return new User(null, email, password, new HashSet<>(initialRoles), true);
    }

    public static User reconstitute(UUID id, Email email, Password password, Set<Role> roles, boolean enabled) {
        return new User(id, email, password, roles, enabled);
    }

    public void changePassword(Password newPassword) {
        this.password = Objects.requireNonNull(newPassword, "New password cannot be null");
    }

    public void addRole(Role role) {
        Objects.requireNonNull(role, "Role to add cannot be null");
        this.roles.add(role);
    }

    public User removeRole(Role role) {
        Objects.requireNonNull(role, "Role to remove cannot be null");
        this.roles.remove(role);
        return this;
    }

    public User disable() {
        this.enabled = false;
        return this;
    }

    public User enable() {
        this.enabled = true;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    //TODO(Check getAuthorities IDE warning later)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password.getHashedPassword();
    }

    //TODO(Check getUsername IDE warning later)
    @Override
    public String getUsername() {
        return this.email.getValue();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}