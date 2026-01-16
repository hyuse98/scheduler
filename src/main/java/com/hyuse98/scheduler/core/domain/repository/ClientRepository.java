package com.hyuse98.scheduler.core.domain.repository;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.Optional;

public interface ClientRepository {

    void save (Client client);

    Optional<Client> findByName(String name);
}
