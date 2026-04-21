package dev.vetyutnev.eventmanagerplatform.security;

public record TokenPayload(
        Long userId,
        String login,
        String role
) {
}
