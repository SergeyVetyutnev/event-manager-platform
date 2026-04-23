package dev.vetyutnev.eventmanagerplatform.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
}
