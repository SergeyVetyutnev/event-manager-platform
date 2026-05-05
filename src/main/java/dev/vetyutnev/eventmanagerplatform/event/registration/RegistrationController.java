package dev.vetyutnev.eventmanagerplatform.event.registration;

import dev.vetyutnev.eventmanagerplatform.event.EventDto;
import dev.vetyutnev.eventmanagerplatform.event.EventMapper;
import dev.vetyutnev.eventmanagerplatform.security.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final EventMapper eventMapper;
    private final RegistrationService registrationService;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> register(
            @PathVariable Long eventId,
            @AuthenticationPrincipal TokenPayload payload
            ) {
        log.info("HTTP POST /events/registrations/{} - регистрация пользователя на мероприятие", eventId);

        registrationService.register(eventId, payload);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable Long eventId,
            @AuthenticationPrincipal TokenPayload payload
    ) {
        log.info("HTTP DELETE /events/registrations/cancel/{} - отмена регистрации", eventId);

        registrationService.cancelRegistration(eventId, payload);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyRegistrations(@AuthenticationPrincipal TokenPayload payload) {
        log.info("HTTP GET /events/registrations/my - получение регистраций пользователя {}",
                payload.userId());

        List<EventDto> response = registrationService.getMyRegistrations(payload).stream()
                .map(eventMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}
