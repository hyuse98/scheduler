package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.mapper;

import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import com.hyuse98.scheduler.iam.domain.model.vo.Email;
import com.hyuse98.scheduler.iam.domain.model.vo.Password;
import com.hyuse98.scheduler.iam.domain.model.vo.Role;
import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class},
        imports = {Email.class, Password.class, User.class}
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "password", target = "password")
    UserJpaEntity toEntity(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    default User toDomain(UserJpaEntity userJpaEntity) {
        if (userJpaEntity == null) {
            return null;
        }

        return User.reconstitute(
                userJpaEntity.getId(),
                Email.of(userJpaEntity.getEmail()),
                Password.fromHashed(userJpaEntity.getPassword()),
                userJpaEntity.getRoles().stream()
                        .map(r -> Role.valueOf(r.getName().name()))
                        .collect(java.util.stream.Collectors.toSet()),
                userJpaEntity.isEnabled()
        );
    }

}
