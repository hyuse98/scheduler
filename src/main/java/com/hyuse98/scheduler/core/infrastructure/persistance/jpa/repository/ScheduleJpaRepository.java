package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ScheduleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleJpaEntity, UUID> {

    List<ScheduleJpaEntity> findByClientId(UUID clientId);

    List<ScheduleJpaEntity> findByServiceProviderId(UUID serviceProviderId);
}