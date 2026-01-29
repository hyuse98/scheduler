package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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
    public boolean existsById(UUID id) {
        return clientJpaRepository.existsById(id);
    }

    @Override
    public Optional<Client> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return Optional.empty();
    }
}
