package dev.vetyutnev.eventmanagerplatform.event.registration.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    List<RegistrationEntity> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"event"})
    List<RegistrationEntity> findAllWithEventByUserId(Long userId);

    @Query("SELECT r.userId FROM RegistrationEntity r WHERE r.event.id = :eventId")
    List<Long> findUserIdsByEventId(@Param("eventId") Long eventId);

}
