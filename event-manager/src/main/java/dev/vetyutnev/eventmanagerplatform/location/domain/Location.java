package dev.vetyutnev.eventmanagerplatform.location.domain;

import java.io.Serializable;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) implements Serializable {
}
