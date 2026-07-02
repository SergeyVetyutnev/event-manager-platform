package dev.vetyutnev.notificator.notification.domain;

import java.time.OffsetDateTime;

public record Notification(
        Long id,
        Long userId,
        String type,
        Long eventId,
        OffsetDateTime createdAt,
        Boolean isRead,
        String message,
        NotificationPayload payload
) {}
