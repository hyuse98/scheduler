package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.repository;

import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;

    public ClientRepositoryImpl(ClientJpaRepository clientJpaRepository, ClientEntityMapper clientMapper) {
        this.clientJpaRepository = clientJpaRepository;
        this.clientEntityMapper = clientMapper;
    }

    @Override
    public void save(Client client) {

        var entity = clientEntityMapper.toEntity(client);
        clientJpaRepository.save(entity);
    }

    @Override
    public Optional<Client> findByName(String name) {
        return Optional.empty();
    }
}
