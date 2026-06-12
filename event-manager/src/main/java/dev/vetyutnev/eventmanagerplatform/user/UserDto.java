package dev.vetyutnev.eventmanagerplatform.user;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String login,
        Integer age,
        UserRole role
) {}
