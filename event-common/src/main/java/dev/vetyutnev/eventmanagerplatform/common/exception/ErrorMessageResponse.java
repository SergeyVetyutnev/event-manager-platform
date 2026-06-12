package dev.vetyutnev.eventmanagerplatform.common.exception;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}
