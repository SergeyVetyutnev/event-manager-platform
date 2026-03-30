package dev.vetyutnev.eventmanagerplatform.location;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    Location toDomain(LocationDto dto);
    LocationDto toDto(Location domain);

    Location toDomain(LocationEntity entity);
    LocationEntity toEntity(Location domain);

    void updateEntityFromDto(Location domain, @MappingTarget LocationEntity entity);
}
