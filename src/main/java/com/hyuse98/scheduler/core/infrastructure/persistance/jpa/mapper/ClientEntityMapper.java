package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ClientJpaEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientEntityMapper {

    ClientJpaEntity toEntity(Client client);

    ClientDto toDto(Client client);

    /**
     * ObjectFactory para Reconstituição (Base de Dados -> Domínio)
     * Utiliza o método 'reconstitute' que ignora validações de criação
     * e apenas remonta o objeto que já existe no mundo real.
     */
    @ObjectFactory
    default Client createClient(ClientJpaEntity entity) {
        return Client.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getPhoneNumber(),
                entity.getName(),
                entity.getBirthday(),
                entity.getAddress(),
                entity.getCreatedAt(),
                entity.getIsActive(),
                entity.getCNS()
        );
    }

    /**
     * ObjectFactory para Criação (DTO -> Domínio)
     * Utiliza o método 'create' que dispara o validate() para garantir
     * que os dados vindos do utilizador são válidos.
     */
    @ObjectFactory
    default Client createClient(ClientDto dto) {
        return Client.create(
                dto.id(),
                dto.email(),
                dto.password(),
                dto.phoneNumber(),
                dto.name(),
                dto.birthday(),
                dto.address(),
                dto.createdAt(),
                dto.isActive(),
                dto.CNS()
        );
    }

    Client toDomain(ClientDto clientDto);
    Client toDomain(ClientJpaEntity clientJpaEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientJpaEntity partialUpdate(ClientDto clientDto, @MappingTarget ClientJpaEntity clientJpaEntity);
}