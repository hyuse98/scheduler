package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.Optional;
import java.util.UUID;

public interface GetClientUseCase {

    Client execute(UUID id);

    Client execute(String email);
}
