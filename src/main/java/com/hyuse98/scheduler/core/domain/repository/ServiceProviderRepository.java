package com.hyuse98.scheduler.core.domain.repository;

import com.hyuse98.scheduler.core.domain.model.ServiceProvider;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ServiceProviderRepository {

    ServiceProvider save(ServiceProvider serviceProvider);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    Optional<ServiceProvider> findByEmail(String email);

    Optional<ServiceProvider> findById(UUID id);

    Collection<ServiceProvider> findAll();
}