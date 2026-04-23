package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.usecases.schedule.DeleteScheduleUseCase;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteScheduleUseCaseImpl implements DeleteScheduleUseCase {

    private final ScheduleRepository scheduleRepository;

    public DeleteScheduleUseCaseImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        scheduleRepository.deleteById(id);
    }
}
