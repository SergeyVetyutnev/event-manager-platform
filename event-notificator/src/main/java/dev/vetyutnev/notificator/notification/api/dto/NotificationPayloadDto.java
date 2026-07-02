package dev.vetyutnev.notificator.notification.api.dto;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record NotificationPayloadDto(
        UUID messageId,
        String eventType,
        OffsetDateTime occurredAt,
        Long changedById,
        Long ownerId,
        String eventName,
        List<ChangeItem> changes
) {
}
