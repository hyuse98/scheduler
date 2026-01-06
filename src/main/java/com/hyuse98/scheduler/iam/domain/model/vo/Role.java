package com.hyuse98.scheduler.iam.domain.model.vo;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SUPPORT;

    @Override
    public String getAuthority() {
        return name();
    }
}
