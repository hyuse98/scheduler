package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.Optional;
import java.util.UUID;

public interface GetClientUseCase {

    Optional<Client> execute(UUID id);

    Optional<Client> execute(String email);
}
