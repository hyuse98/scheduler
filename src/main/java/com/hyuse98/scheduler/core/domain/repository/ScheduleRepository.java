package com.hyuse98.scheduler.core.domain.repository;

import com.hyuse98.scheduler.core.domain.model.Schedule;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);

    Collection<Schedule> findByClientId(UUID clientId);

    Collection<Schedule> findByServiceProviderId(UUID serviceProviderId);

    void deleteById(UUID id);
}