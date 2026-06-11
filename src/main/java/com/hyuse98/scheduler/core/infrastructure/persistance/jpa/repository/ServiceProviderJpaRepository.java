package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ServiceProviderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceProviderJpaRepository extends JpaRepository<ServiceProviderJpaEntity, UUID> {
    Optional<ServiceProviderJpaEntity> findByEmail(String email);
}