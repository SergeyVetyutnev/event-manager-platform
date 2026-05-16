package dev.vetyutnev.eventmanagerplatform.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record EventUpdateRequestDto(
        @NotBlank(message = "Название мероприятия не может быть пустым")
        String name,

        @NotNull(message = "Количество мест обязательно")
        @Min(value = 1, message = "Должно быть хотя бы 1 место")
        Integer maxPlaces,

        @NotNull(message = "Дата обязательна")
        @Future(message = "Мероприятие должно быть в будущем")
        OffsetDateTime date,

        @NotNull(message = "Стоимость обязательна")
        @Min(value = 1, message = "Стоимость должна быть больше 0")
        Integer cost,

        @NotNull(message = "Длительность обязательна")
        @Min(value = 30, message = "Длительность не может быть меньше 30 минут")
        Integer duration,

        @NotNull(message = "ID локации обязателен")
        Long locationId
) {
}
