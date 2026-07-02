package dev.vetyutnev.eventmanagerplatform.event.mapper;

import dev.vetyutnev.eventmanagerplatform.event.api.dto.EventCreateRequestDto;
import dev.vetyutnev.eventmanagerplatform.event.api.dto.EventDto;
import dev.vetyutnev.eventmanagerplatform.event.api.dto.EventUpdateRequestDto;
import dev.vetyutnev.eventmanagerplatform.event.domain.Event;
import dev.vetyutnev.eventmanagerplatform.event.repository.EventEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventMapper {
    EventEntity toEntity(Event domain);

    Event toDomain(EventEntity entity);

    Event toDomain(EventCreateRequestDto requestDto);

    Event toDomain(EventUpdateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "occupiedPlaces", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    void updateEntityFromDomain(Event eventDomain, @MappingTarget EventEntity entity);

    EventDto toDto(Event domain);
}
