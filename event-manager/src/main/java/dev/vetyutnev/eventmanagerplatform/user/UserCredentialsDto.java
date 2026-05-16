package dev.vetyutnev.eventmanagerplatform.user;

import jakarta.validation.constraints.NotBlank;

public record UserCredentialsDto(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {
}
