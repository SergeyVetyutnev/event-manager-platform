package dev.vetyutnev.eventmanagerplatform.event;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;
import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import dev.vetyutnev.eventmanagerplatform.common.utils.DiffUtils;
import dev.vetyutnev.eventmanagerplatform.event.exception.EventNotFoundException;
import dev.vetyutnev.eventmanagerplatform.event.exception.EventValidationException;
import dev.vetyutnev.eventmanagerplatform.event.kafka.EventPublisherService;
import dev.vetyutnev.eventmanagerplatform.event.registration.RegistrationRepository;
import dev.vetyutnev.eventmanagerplatform.location.Location;
import dev.vetyutnev.eventmanagerplatform.location.LocationService;
import dev.vetyutnev.eventmanagerplatform.security.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventMapper eventMapper;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final EventPermissionService eventPermissionService;
    private final RegistrationRepository registrationRepository;
    private final EventPublisherService eventPublisherService;

    @Transactional
    public Event createEvent(Event eventDomain, TokenPayload currentUser) {
        log.info("Создание нового мероприятия {}", eventDomain.name());

        Location location = locationService.getLocationById(eventDomain.locationId());
        if (location.capacity() < eventDomain.maxPlaces()) {
            throw new EventValidationException(
                    "Вместимость локации (%s) меньше заявленного количества мест (%s)"
                            .formatted(location.capacity(), eventDomain.maxPlaces())
            );
        }

        EventEntity entity = eventMapper.toEntity(eventDomain);
        entity.setOwnerId(currentUser.userId());
        entity.setStatus(EventStatus.WAIT_START);
        entity.setOccupiedPlaces(0);

        EventEntity savedEntity = eventRepository.save(entity);
        return eventMapper.toDomain(savedEntity);
    }

    public Event getById(Long id) {
        var entity = getEntityByIdOrThrow(id);
        return eventMapper.toDomain(entity);
    }

    @Transactional()
    public Event updateEvent(Long eventId, Event newDomain, TokenPayload currentUser) {
        log.info("Обновление мероприятия с id: {}", eventId);

        var existingEntity = getEntityByIdOrThrow(eventId);

        eventPermissionService.verifyModificationAccess(existingEntity.getOwnerId(), currentUser);

        if (newDomain.maxPlaces() < existingEntity.getOccupiedPlaces()) {
            throw new EventValidationException(
                    "Новое количество мест (%s)  не может быть меньше занятых мест (%s)"
                            .formatted(newDomain.maxPlaces(), existingEntity.getOccupiedPlaces()));
        }

        var newLocation = locationService.getLocationById(newDomain.locationId());
        if (newLocation.capacity() < newDomain.maxPlaces()) {
            throw new EventValidationException(
                    "Вместимость новой локации (%s) меньше вместимости мероприятия (%s)"
                            .formatted(newLocation.capacity(), newDomain.maxPlaces()));
        }

        var oldEventDomain = eventMapper.toDomain(existingEntity);

        eventMapper.updateEntityFromDomain(newDomain, existingEntity);
        var updatedEntity = eventRepository.save(existingEntity);

        var newEventDomain = eventMapper.toDomain(updatedEntity);
        List<ChangeItem> changes = DiffUtils.generateChanges(oldEventDomain, newEventDomain);

        if (!changes.isEmpty()){
            List<Long> subscribers = registrationRepository.findUserIdsByEventId(eventId);

            var message = EventChangeKafkaMessage.builder()
                    .messageId(UUID.randomUUID())
                    .eventType("EVENT_UPDATED")
                    .eventId(eventId)
                    .occurredAt(OffsetDateTime.now())
                    .ownerId(updatedEntity.getOwnerId())
                    .changedById(currentUser.userId())
                    .eventName(updatedEntity.getName())
                    .subscribers(subscribers)
                    .changes(changes)
                    .build();

            eventPublisherService.publishEventChange(message);
        }

        return newEventDomain;
    }

    @Transactional
    public void cancelEvent(Long eventId, TokenPayload currentUser) {
        log.info("Отмена мероприятия с id: {}", eventId);

        var existingEntity = getEntityByIdOrThrow(eventId);

        eventPermissionService.verifyModificationAccess(existingEntity.getOwnerId(), currentUser);

        if (existingEntity.getStatus() != EventStatus.WAIT_START) {
            throw new EventValidationException("Невозможно отменить мероприятие в статусе: %s"
                    .formatted(existingEntity.getStatus()));
        }

        String oldStatus = existingEntity.getStatus().name();

        existingEntity.setStatus(EventStatus.CANCELLED);
        eventRepository.save(existingEntity);

        var changeItem = ChangeItem.builder()
                .field("status")
                .oldValue(oldStatus)
                .newValue(existingEntity.getStatus().name())
                .build();

        List<Long> subscribers = registrationRepository.findUserIdsByEventId(eventId);

        var message = EventChangeKafkaMessage.builder()
                .messageId(UUID.randomUUID())
                .eventType("EVENT_CANCELLED")
                .eventId(eventId)
                .occurredAt(OffsetDateTime.now())
                .ownerId(existingEntity.getOwnerId())
                .changedById(currentUser.userId())
                .eventName(existingEntity.getName())
                .subscribers(subscribers)
                .changes(List.of(changeItem))
                .build();

        eventPublisherService.publishEventChange(message);
    }

    //TODO: пагниация
    public List<Event> searchEvents(EventSearchRequestDto filter) {
        log.info("Поиск мероприятий по фильтру");

        Specification<EventEntity> spec = EventSpecification.withFilter(filter);

        return eventRepository.findAll(spec).stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    public List<Event> getMyEvents(TokenPayload currentUser) {
        log.info("Запрос мероприятий пользователя с id: {}", currentUser.userId());

        return eventRepository.findAllByOwnerId(currentUser.userId()).stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    private EventEntity getEntityByIdOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(
                        "Мероприятие с id %s не найдено".formatted(id)));
    }

}
