package dev.vetyutnev.eventmanagerplatform.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LocationDto(
    Long id,

    @NotBlank(message = "Название локации не может быть пустым")
    String name,

    @NotBlank(message = "Адрес локации не может быть пустым")
    String address,

    @NotNull(message = "Вместимость локации должна быть указана")
    @Min(value = 5, message = "Вместимость должна быть больше или равна 5")
    Integer capacity,

    String description
    ){

            }
