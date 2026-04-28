package dev.vetyutnev.eventmanagerplatform.registration;

import dev.vetyutnev.eventmanagerplatform.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    List<RegistrationEntity> findAllByUserId(Long userId);
}
