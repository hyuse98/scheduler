package com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.application.dto.ScheduleResponse;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.entities.ScheduleJpaEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleEntityMapper {

    @Mapping(target = "status", expression = "java(schedule.getStatus() != null ? schedule.getStatus().name() : null)")
    ScheduleJpaEntity toEntity(Schedule schedule);

    Schedule toDomain(ScheduleJpaEntity entity);

    @Mapping(target = "status", expression = "java(domain.getStatus() != null ? domain.getStatus().name() : null)")
    ScheduleResponse toResponse(Schedule domain);

    @ObjectFactory
    default Schedule reconstituteSchedule(ScheduleJpaEntity entity) {
        return Schedule.reconstitute(
                entity.getId(),
                entity.getClientId(),
                entity.getServiceProviderId(),
                entity.getServiceType(),
                entity.getDescription(),
                entity.getScheduledAt(),
                entity.getStatus()
        );
    }

    @ObjectFactory
    default Schedule createNewSchedule(CreateScheduleRequest request) {
        return Schedule.create(
                UUID.randomUUID(), // Gera um novo UUID na criação
                request.clientId(),
                request.serviceProviderId(),
                request.serviceType(),
                request.description(),
                request.scheduledAt()
        );
    }
}