package dev.vetyutnev.eventmanagerplatform.location;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    LocationDto toDto(LocationEntity entity);

    LocationEntity toEntity(LocationDto dto);
}
