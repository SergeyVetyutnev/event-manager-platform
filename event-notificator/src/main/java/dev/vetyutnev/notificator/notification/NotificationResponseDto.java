package dev.vetyutnev.notificator.notification;

import java.time.OffsetDateTime;

public record NotificationResponseDto(
        Long notificationId,
        String type,
        Long eventId,
        OffsetDateTime createdAt,
        Boolean isRead,
        String message,
        NotificationPayloadDto payload
) {
}
