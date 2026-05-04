package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository;

import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {
    Optional<RoleJpaEntity> findByName(RoleJpaEntity.RoleName name);
}
