package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper;

import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RoleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    default Role toDomain(RoleJpaEntity entity) {
        if (entity == null || entity.getName() == null) {
            return null;
        }
        return Role.valueOf(entity.getName().name());
    }

    default RoleJpaEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        try {
            RoleJpaEntity.RoleName roleName = RoleJpaEntity.RoleName.valueOf(domain.name());
            return new RoleJpaEntity(roleName);
        } catch (IllegalArgumentException e) {
            // TODO
            // Handle cases where Role enum has values not present in RoleJpaEntity.RoleName
            // For now, we'll return null or you could throw a custom exception
            return null;
        }
    }
}
