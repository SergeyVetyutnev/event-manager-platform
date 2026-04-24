package dev.vetyutnev.eventmanagerplatform.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDto(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Long locationId,
        EventStatus eventStatus
) {
}
