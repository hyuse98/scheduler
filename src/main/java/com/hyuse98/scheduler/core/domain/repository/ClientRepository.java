package com.hyuse98.scheduler.core.domain.repository;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    Optional<Client> findByName(String name);

    Optional<Client> findByEmail(String email);

    Optional<Client> findById(UUID id);

    Collection<Client> findAll();

//    Collection<Client> findAllByOrderByNameAsc();
//
//    Collection<Client> findAllByOrderByEmailAsc();
//
//    Collection<Client> findAllByOrderByNameDesc();
//
//    Collection<Client> findAllByOrderByEmailDesc();
}
