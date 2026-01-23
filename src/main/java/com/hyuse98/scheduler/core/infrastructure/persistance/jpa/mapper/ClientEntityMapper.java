package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ClientJpaEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientEntityMapper {

    ClientJpaEntity toEntity(Client client);

    ClientDto toDto(Client client);

    Client toDomain(ClientDto clientDto);

    Client toDomain(ClientJpaEntity clientJpaEntity);

    @ObjectFactory
    default Client createClient(ClientJpaEntity entity) {
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
    default Client createClient(ClientDto dto) {
        return Client.create(
                dto.id(),
                dto.email(),
                dto.name(),
                dto.phoneNumber(),
                dto.birthday(),
                dto.address(),
                dto.cns(),
                dto.createdAt(),
                dto.isActive()
        );
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientJpaEntity partialUpdate(ClientDto clientDto, @MappingTarget ClientJpaEntity clientJpaEntity);
}