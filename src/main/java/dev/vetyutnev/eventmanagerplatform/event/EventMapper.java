package dev.vetyutnev.eventmanagerplatform.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    EventEntity toEntity(Event domain);

    Event toDomain(EventEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "occupiedPlaces", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDomain(Event eventDomain, @MappingTarget EventEntity entity);
}
