package dev.vetyutnev.eventmanagerplatform.common.security;

public record TokenPayload(
        Long userId,
        String login,
        String role
) {
}
