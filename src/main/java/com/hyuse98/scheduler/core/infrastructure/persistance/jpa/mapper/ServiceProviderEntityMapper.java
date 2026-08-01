package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ServiceProviderJpaEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceProviderEntityMapper {

    ServiceProviderJpaEntity toEntity(ServiceProvider serviceProvider);

    ServiceProvider toDomain(ServiceProviderJpaEntity entity);

    ServiceProviderResponse toResponse(ServiceProvider domain);

    @ObjectFactory
    default ServiceProvider reconstituteServiceProvider(ServiceProviderJpaEntity entity) {
        return ServiceProvider.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getBirthday(),
                entity.getAddress(),
                entity.getExpertise(),
                entity.getRegistry(),
                entity.getCreatedAt(),
                entity.getIsActive()
        );
    }
}