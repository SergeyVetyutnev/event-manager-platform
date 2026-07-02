package dev.vetyutnev.eventmanagerplatform.event.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record Event(
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
) implements Serializable {
}
