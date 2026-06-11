package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository.impl;

import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository.ServiceProviderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ServiceProviderRepositoryImpl implements ServiceProviderRepository {

    private final ServiceProviderJpaRepository jpaRepository;
    private final ServiceProviderEntityMapper mapper;

    public ServiceProviderRepositoryImpl(ServiceProviderJpaRepository jpaRepository, ServiceProviderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ServiceProvider save(ServiceProvider serviceProvider) {
        var entity = mapper.toEntity(serviceProvider);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<ServiceProvider> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<ServiceProvider> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Collection<ServiceProvider> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}