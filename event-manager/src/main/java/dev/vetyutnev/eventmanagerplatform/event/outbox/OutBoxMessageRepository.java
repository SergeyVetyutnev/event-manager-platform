package dev.vetyutnev.eventmanagerplatform.event.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface OutBoxMessageRepository extends JpaRepository<OutboxMessageEntity, Long> {

    List<OutboxMessageEntity> findAllByStatusIsOrderByCreatedAtAsc(OutboxStatus status);

    List<OutboxMessageEntity> findAllByCreatedAtBefore(OffsetDateTime createdAtBefore);

    @Modifying
    @Query("DELETE FROM OutboxMessageEntity o WHERE o.status IN :statuses AND o.createdAt < :cutoffDate")
    int deleteOldMessages(@Param("statuses") List<OutboxStatus> statuses, @Param("cutoffDate") OffsetDateTime cutoffDate);
}
