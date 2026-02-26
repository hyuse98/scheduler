package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository.impl;

import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ScheduleEntityMapper;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository.ScheduleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository jpaRepository;
    private final ScheduleEntityMapper mapper;

    public ScheduleRepositoryImpl(ScheduleJpaRepository jpaRepository, ScheduleEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Schedule save(Schedule schedule) {
        var entity = mapper.toEntity(schedule);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Schedule> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Collection<Schedule> findByClientId(UUID clientId) {
        return jpaRepository.findByClientId(clientId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Schedule> findByServiceProviderId(UUID serviceProviderId) {
        return jpaRepository.findByServiceProviderId(serviceProviderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}