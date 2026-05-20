package dev.vetyutnev.eventmanagerplatform.common.kafka;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record EventChangeKafkaMessage(
        UUID messageId,
        String eventType,
        Long eventId,
        OffsetDateTime occurredAt,
        Long ownerId,
        Long changedById,
        String eventName,
        List<Long> subscribers,
        List<ChangeItem> changes


) {
}
