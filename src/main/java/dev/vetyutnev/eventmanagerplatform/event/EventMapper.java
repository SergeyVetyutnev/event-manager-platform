package dev.vetyutnev.eventmanagerplatform.event;

import org.jspecify.annotations.Nullable;
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

    void updateEntityFromDomain(Event eventDomain, @MappingTarget EventEntity entity);

    EventDto toDto(Event domain);
}
