package com.hyuse98.scheduler.core.application.usecases.schedule;

import com.hyuse98.scheduler.core.domain.model.Schedule;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface ListSchedulesUseCase {
    @Transactional
    List<Schedule> findByClientId(UUID clientId);

    @Transactional
    List<Schedule> findByServiceProviderId(UUID serviceProviderId);
}
