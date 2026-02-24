package com.hyuse98.scheduler.core.application.usecases.schedule;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import jakarta.transaction.Transactional;

public interface CreateScheduleUseCase {
    @Transactional
    Schedule execute(String clientEmail, CreateScheduleRequest request);
}
