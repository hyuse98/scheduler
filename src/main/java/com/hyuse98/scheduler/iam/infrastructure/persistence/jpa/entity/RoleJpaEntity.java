package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "roles", schema = "iam")
public class RoleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private RoleName name;

    public RoleJpaEntity(RoleName name) {
        this.name = name;
    }

    public RoleJpaEntity() {
    }

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
