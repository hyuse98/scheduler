package com.hyuse98.scheduler.core.application.usecases.schedule;

import java.util.UUID;

public interface DeleteScheduleUseCase {
    void execute(UUID id);
}
