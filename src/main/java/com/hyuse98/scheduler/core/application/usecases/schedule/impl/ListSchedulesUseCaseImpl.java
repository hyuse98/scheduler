package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.usecases.schedule.ListSchedulesUseCase;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListSchedulesUseCaseImpl implements ListSchedulesUseCase {

    private final ScheduleRepository scheduleRepository;

    public ListSchedulesUseCaseImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    @Override
    public List<Schedule> findByClientId(UUID clientId) {
        return scheduleRepository.findByClientId(clientId).stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<Schedule> findByServiceProviderId(UUID serviceProviderId) {
        return scheduleRepository.findByServiceProviderId(serviceProviderId).stream().collect(Collectors.toList());
    }
}