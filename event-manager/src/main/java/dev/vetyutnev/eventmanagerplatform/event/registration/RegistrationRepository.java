package dev.vetyutnev.eventmanagerplatform.event.registration;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    List<RegistrationEntity> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"event"})
    List<RegistrationEntity> findAllWithEventByUserId(Long userId);
}
