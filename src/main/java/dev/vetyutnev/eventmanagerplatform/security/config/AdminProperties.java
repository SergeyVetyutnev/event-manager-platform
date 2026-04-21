package dev.vetyutnev.eventmanagerplatform.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.default-admin")
public record AdminProperties(
        String login,
        String password,
        Integer age
) {
}
