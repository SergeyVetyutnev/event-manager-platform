package dev.vetyutnev.eventmanagerplatform.event;

import dev.vetyutnev.eventmanagerplatform.event.exception.EventNotFoundException;
import dev.vetyutnev.eventmanagerplatform.event.exception.EventValidationException;
import dev.vetyutnev.eventmanagerplatform.location.Location;
import dev.vetyutnev.eventmanagerplatform.location.LocationService;
import dev.vetyutnev.eventmanagerplatform.security.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventMapper eventMapper;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final EventPermissionService eventPermissionService;

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

        eventMapper.updateEntityFromDomain(newDomain, existingEntity);

        var savedEntity = eventRepository.save(existingEntity);
        return eventMapper.toDomain(savedEntity);
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

        existingEntity.setStatus(EventStatus.CANCELLED);
        eventRepository.save(existingEntity);
    }

    //TODO: пагниация
    public List<Event> searchEvents(EventSearchRequestDto filter) {
        log.info("Поиск мероприятий по фильтру");

        Specification<EventEntity> spec = EventSpecification.withFilter(filter);

        return eventRepository.findAll(spec).stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    public List<Event> getMyEvents(TokenPayload currentUser){
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
