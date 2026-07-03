package dev.vetyutnev.eventmanagerplatform.user.domain;

import lombok.Builder;

@Builder
public record User (
        Long id,
        String login,
        String password,
        Integer age,
        UserRole role
)
{}
