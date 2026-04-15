package com.hyuse98.scheduler.iam.application.usecase;

import java.util.UUID;

public interface UpdateUserStatusUseCase {
    public void execute(UUID userId, boolean newStatus);
}
