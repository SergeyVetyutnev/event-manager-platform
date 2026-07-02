package dev.vetyutnev.eventmanagerplatform.event.api.dto;

import dev.vetyutnev.eventmanagerplatform.event.domain.EventStatus;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EventDto(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        OffsetDateTime date,
        Integer cost,
        Integer duration,
        Long locationId,
        EventStatus status
) {
}
