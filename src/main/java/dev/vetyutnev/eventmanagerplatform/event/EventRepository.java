package dev.vetyutnev.eventmanagerplatform.event;

import jakarta.persistence.LockModeType;
import org.hibernate.dialect.lock.LockingStrategy;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    Optional<EventEntity> findById(Long id);

    List<EventEntity> findAllByOwnerId(Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM event WHERE e.id = :id")
    Optional<EventEntity> findByIdWithLock(@Param("id") Long id);

    @Query(value = "SELECT id FROM event WHERE status = :status AND date <= CURRENT_TIMESTAMP",
            nativeQuery = true)
    List<Long> findEventsToStart(@Param("status") String status);

    @Query(value = """
        SELECT id FROM event
        WHERE status = :status
        AND date + (duration * interval '1 minute') <= CURRENT_TIMESTAMP
        """, nativeQuery = true)
    List<Long> findEventsToFinish(@Param("status") String status);

    @Modifying
    @Query("UPDATE EventEntity e SET e.status = :newStatus WHERE e.id = :id")
    void changeStatus(@Param("id") Long id, @Param("newStatus") EventStatus newStatus);

}
