package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;

    public ClientRepositoryImpl(ClientJpaRepository clientJpaRepository, ClientEntityMapper clientMapper) {
        this.clientJpaRepository = clientJpaRepository;
        this.clientEntityMapper = clientMapper;
    }

    @Override
    public Client save(Client client) {
        var entity = clientEntityMapper.toEntity(client);
        clientJpaRepository.save(entity);
        return clientEntityMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        clientJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return clientJpaRepository.existsById(id);
    }

    @Override
    public Optional<Client> findByName(String name) {
        return clientJpaRepository.findByName(name)
                .map(clientEntityMapper::toDomain);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientJpaRepository.findByEmail(email)
                .map(clientEntityMapper::toDomain);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        var entity = clientJpaRepository.findById(id);
        return entity.map(clientEntityMapper::toDomain);
    }

    @Override
    public Collection<Client> findAll() {
        return clientJpaRepository.findAll()
                .stream()
                .map(clientEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
