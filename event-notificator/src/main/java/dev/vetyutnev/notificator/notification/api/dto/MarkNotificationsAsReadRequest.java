package dev.vetyutnev.notificator.notification.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MarkNotificationsAsReadRequest(
        @NotNull(message = "Список ID не должен быть null")
        List<Long> notificationIds
) {
}
