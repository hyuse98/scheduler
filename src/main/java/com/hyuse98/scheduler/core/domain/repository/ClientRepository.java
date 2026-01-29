package com.hyuse98.scheduler.core.domain.repository;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Client save (Client client);

    boolean existsById (UUID id);

    Optional<Client> findByName(String name);

    Optional<Client> findByEmail(String email);
}
