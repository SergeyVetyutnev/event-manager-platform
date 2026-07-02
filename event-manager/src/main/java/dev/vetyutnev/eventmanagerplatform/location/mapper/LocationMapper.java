package dev.vetyutnev.eventmanagerplatform.location.mapper;

import dev.vetyutnev.eventmanagerplatform.location.api.dto.LocationDto;
import dev.vetyutnev.eventmanagerplatform.location.domain.Location;
import dev.vetyutnev.eventmanagerplatform.location.repository.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    Location toDomain(LocationDto dto);
    LocationDto toDto(Location domain);

    Location toDomain(LocationEntity entity);
    LocationEntity toEntity(Location domain);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(Location domain, @MappingTarget LocationEntity entity);
}
