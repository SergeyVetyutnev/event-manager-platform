package dev.vetyutnev.notificator.notification.domain;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record NotificationPayload(
        UUID messageId,
        String eventType,
        Long eventId,
        OffsetDateTime occurredAt,
        Long changedById,
        Long ownerId,
        String eventName,
        List<ChangeItem> changes
) {}
