package dev.vetyutnev.eventmanagerplatform.event;

import org.jspecify.annotations.Nullable;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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

    default LocalDateTime map(java.time.OffsetDateTime offsetDateTime){
        if (offsetDateTime == null){
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

    default OffsetDateTime map(LocalDateTime localDateTime){
        if (localDateTime == null){
            return null;
        }
        return localDateTime.atOffset(java.time.ZoneOffset.UTC);
    }
}
