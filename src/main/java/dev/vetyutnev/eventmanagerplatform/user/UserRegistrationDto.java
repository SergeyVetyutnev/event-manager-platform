package dev.vetyutnev.eventmanagerplatform.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegistrationDto(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        String password,

        @NotNull(message = "Возраст обязателен")
        @Min(value = 18, message = "Возраст должен быть не меньше 18 лет")
        Integer age
) {
}
