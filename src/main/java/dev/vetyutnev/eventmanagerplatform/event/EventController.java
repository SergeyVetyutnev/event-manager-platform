package dev.vetyutnev.eventmanagerplatform.event;

import dev.vetyutnev.eventmanagerplatform.security.TokenPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @Valid @RequestBody EventCreateRequestDto request,
            @AuthenticationPrincipal TokenPayload payload
    ) {
        log.info("HTTP POST /events - создание мероприятия: {}", request.name());

        var eventDomain = eventService.createEvent(
                eventMapper.toDomain(request),
                payload
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toDto(eventDomain));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId){
        log.info("HTTP GET /events/{} - получение мероприятия");

        return ResponseEntity.ok(
                eventMapper.toDto(
                        eventService.getById(eventId)
                )
        );
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventUpdateRequestDto request,
            @AuthenticationPrincipal TokenPayload payload
    ) {
        log.info("HTTP PUT /events/{} - обновление мероприятия", eventId);

        var updatedEvent = eventService.updateEvent(eventId,
                eventMapper.toDomain(request),
                payload
                );

        return ResponseEntity.ok(eventMapper.toDto(updatedEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal TokenPayload payload
            ){
        log.info("HTTP DELETE /events/{} - отмена мероприятия", eventId);

        eventService.cancelEvent(eventId, payload);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvents(@RequestBody EventSearchRequestDto request){
        log.info("HTTP POST /events/search - поиск мероприятий");

        List<EventDto> events = eventService.searchEvents(request).stream()
                .map(eventMapper::toDto)
                .toList();

        return ResponseEntity.ok(events);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents(@AuthenticationPrincipal TokenPayload payload){
        log.info("HTTP GET /events/my - получение своих мероприятий пользователем {}", payload.userId());

        List<EventDto> events = eventService.getMyEvents(payload).stream()
                .map(eventMapper::toDto)
                .toList();

        return ResponseEntity.ok(events);
    }
}
