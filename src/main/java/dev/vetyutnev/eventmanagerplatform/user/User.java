package dev.vetyutnev.eventmanagerplatform.user;

import lombok.Builder;

@Builder
public record User (
        Long id,
        String login,
        String passwordHash,
        Integer age,
        UserRole role
)
{}
