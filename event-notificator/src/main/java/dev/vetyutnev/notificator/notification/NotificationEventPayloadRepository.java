package dev.vetyutnev.notificator.notification;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {

    boolean existsByMessageId(UUID messageId);
}
