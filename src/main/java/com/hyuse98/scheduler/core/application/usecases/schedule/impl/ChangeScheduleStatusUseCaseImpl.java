package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.exceptions.ScheduleNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.schedule.ChangeScheduleStatusUseCase;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.model.ScheduleStatus;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeScheduleStatusUseCaseImpl implements ChangeScheduleStatusUseCase {

    private final ScheduleRepository scheduleRepository;

    public ChangeScheduleStatusUseCaseImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    @Override
    public Schedule execute(UUID scheduleId, String newStatus) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Agendamento não encontrado com o ID: " + scheduleId));

        // Converte a string para o Enum. Caso seja inválido, lançará uma IllegalArgumentException
        ScheduleStatus statusEnum = ScheduleStatus.valueOf(newStatus.toUpperCase());

        schedule.changeStatus(statusEnum);

        return scheduleRepository.save(schedule);
    }
}