package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.ClientResponse;
import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ClientJpaEntity;
import org.mapstruct.*;

import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientEntityMapper {

    ClientJpaEntity toEntity(Client client);
    Client toDomain(ClientJpaEntity clientJpaEntity);

    ClientResponse toResponse(Client client);

    Client toDomain(CreateClientRequest request);

    @ObjectFactory
    default Client reconstituteClient(ClientJpaEntity entity) {
        return Client.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getBirthday(),
                entity.getAddress(),
                entity.getCns(),
                entity.getCreatedAt(),
                entity.getIsActive()
        );
    }

    @ObjectFactory
    default Client createNewClient(CreateClientRequest request) {
        return Client.create(
                request.id(),
                request.name(),
                request.email(),
                request.phoneNumber(),
                request.birthday(),
                request.address(),
                request.cns(),
                new Date(),
                true
        );
    }
}