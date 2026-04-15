package dev.vetyutnev.eventmanagerplatform.security;

public record TokenPayload(
        String login,
        String role
) {
}
