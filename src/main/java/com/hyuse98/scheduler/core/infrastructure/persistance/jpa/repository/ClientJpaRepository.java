package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, UUID> {
}
