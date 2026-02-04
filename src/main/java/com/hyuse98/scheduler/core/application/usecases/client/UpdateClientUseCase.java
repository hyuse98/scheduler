package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;

import java.util.UUID;

public interface UpdateClientUseCase {

    void execute(UUID id, UpdateClientRequest request);
}
