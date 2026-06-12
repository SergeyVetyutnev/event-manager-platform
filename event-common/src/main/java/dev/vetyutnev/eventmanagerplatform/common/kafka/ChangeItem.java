package dev.vetyutnev.eventmanagerplatform.common.kafka;

import lombok.Builder;

@Builder
public record ChangeItem(
        String field,
        Object oldValue,
        Object newValue
) {
}
