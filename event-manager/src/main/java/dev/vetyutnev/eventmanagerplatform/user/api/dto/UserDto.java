package dev.vetyutnev.eventmanagerplatform.user.api.dto;

import dev.vetyutnev.eventmanagerplatform.user.domain.UserRole;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String login,
        Integer age,
        UserRole role
) {}
