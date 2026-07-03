package dev.vetyutnev.notificator.notification.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {

    boolean existsByMessageId(UUID messageId);

    @Modifying
    @Query("""
            DELETE from NotificationEventPayloadEntity p 
            WHERE p.occurredAt < :cutoffDate
            AND NOT EXISTS (SELECT n FROM NotificationEntity n WHERE n.payload = p)      
            """)
    int deleteOrphanedPayloads(@Param("cutoffDate") OffsetDateTime cutoffDate);
}
