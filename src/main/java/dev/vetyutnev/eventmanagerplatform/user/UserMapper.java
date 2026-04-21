package dev.vetyutnev.eventmanagerplatform.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    public User toDomainFromRegistration(UserRegistrationDto registrationDto);

    @Mapping(source = "password", target = "passwordHash")
    UserEntity toEntity(User domain);

    @Mapping(source = "passwordHash", target = "password")
    User toDomain(UserEntity entity);

    UserCredentials toDomain(UserCredentialsDto dto);

    UserDto toDto(User domain);
}
