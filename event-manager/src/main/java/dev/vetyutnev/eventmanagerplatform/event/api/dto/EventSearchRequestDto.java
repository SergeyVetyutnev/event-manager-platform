package dev.vetyutnev.eventmanagerplatform.event.api.dto;

import dev.vetyutnev.eventmanagerplatform.event.domain.EventStatus;

import java.time.OffsetDateTime;

public record EventSearchRequestDto(
        String name,
        Integer placesMin,
        Integer placesMax,
        OffsetDateTime  dateStartAfter,
        OffsetDateTime dateStartBefore,
        Integer costMin,
        Integer costMax,
        Integer durationMin,
        Integer durationMax,
        Long locationId,
        EventStatus eventStatus
) {
}
