package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.CreateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ServiceProviderJpaEntity;
import org.mapstruct.*;

import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceProviderEntityMapper {

    ServiceProviderJpaEntity toEntity(ServiceProvider serviceProvider);

    ServiceProvider toDomain(ServiceProviderJpaEntity entity);

    ServiceProviderResponse toResponse(ServiceProvider domain);

    ServiceProvider toDomain(CreateServiceProviderRequest request);

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

    @ObjectFactory
    default ServiceProvider createNewServiceProvider(CreateServiceProviderRequest request) {
        return ServiceProvider.create(
                request.id(),
                request.name(),
                request.email(),
                request.phoneNumber(),
                request.birthday(),
                request.address(),
                request.expertise(),
                request.registry(),
                new Date(),
                true
        );
    }
}