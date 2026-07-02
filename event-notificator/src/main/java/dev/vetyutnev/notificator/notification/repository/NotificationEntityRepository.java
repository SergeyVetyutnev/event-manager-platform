package dev.vetyutnev.notificator.notification.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {

    @EntityGraph(attributePaths = {"payload"})
    List<NotificationEntity> findAllByUserIdAndIsReadFalse(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE NotificationEntity n
            SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP 
            WHERE n.id IN :ids AND n.userId = :userId
            """)
    void markAsRead(@Param("ids") List<Long> notificationIds, @Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM NotificationEntity n WHERE n.createdAt < :cutoffDate")
    int deleteOldNotifications(@Param("cutoffDate")OffsetDateTime dateTime);

    long countByUserIdAndIsReadFalse(Long userId);
}
