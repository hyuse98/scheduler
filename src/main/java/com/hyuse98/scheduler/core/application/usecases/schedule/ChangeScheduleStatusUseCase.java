package com.hyuse98.scheduler.core.application.usecases.schedule;

import com.hyuse98.scheduler.core.domain.model.Schedule;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface ChangeScheduleStatusUseCase {
    @Transactional
    Schedule execute(UUID scheduleId, String newStatus);
}
