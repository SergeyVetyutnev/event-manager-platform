package dev.vetyutnev.eventmanagerplatform.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "password", target = "password_hash")
    public User toDomainFromRegistration(UserRegistrationDto registrationDto);

    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);

    UserCredentials toDomain(UserCredentialsDto dto);

    UserDto toDto(User domain);
}
