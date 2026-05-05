package dev.vetyutnev.eventmanagerplatform.event.registration;

import dev.vetyutnev.eventmanagerplatform.event.Event;
import dev.vetyutnev.eventmanagerplatform.event.EventMapper;
import dev.vetyutnev.eventmanagerplatform.event.EventRepository;
import dev.vetyutnev.eventmanagerplatform.event.EventStatus;
import dev.vetyutnev.eventmanagerplatform.event.exception.EventNotFoundException;
import dev.vetyutnev.eventmanagerplatform.event.registration.exception.RegistrationException;
import dev.vetyutnev.eventmanagerplatform.security.TokenPayload;
import dev.vetyutnev.eventmanagerplatform.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public void register(Long eventId, TokenPayload currentUser){

        log.info("Пользователь {} пытается зарегистрироваться на событие {}",
                currentUser.userId(), eventId);

        var event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException(
                        "Мероприятие с id %s не найдено".formatted(eventId)));

        if (event.getStatus() != EventStatus.WAIT_START){
            throw new RegistrationException(
                    "Регистрация закрыта. Текущий статус: %s".formatted(event.getStatus()));
        }

        if (event.getOccupiedPlaces() >= event.getMaxPlaces()){
            throw new RegistrationException("Нет свободных мест на данное мероприятие");
        }

        if (registrationRepository.existsByEventIdAndUserId(eventId, currentUser.userId())){
            throw new RegistrationException("Вы уже зарегистрированы на это мероприятие");
        }

        RegistrationEntity registration = RegistrationEntity.builder()
                .event(event)
                .userId(currentUser.userId())
                .createdAt(LocalDateTime.now())
                .build();

        registrationRepository.save(registration);

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        eventRepository.save(event);

        log.info("Пользователь {} успешно зарегистрирован на событие {}",
                currentUser.userId(), eventId);
    }

    @Transactional
    public void cancelRegistration(Long eventId, TokenPayload currentUser){
        log.info("Пользователь {} отменяет регистрацию на мероприятие {}", currentUser.userId(), eventId);

        var registration = registrationRepository.findByEventIdAndUserId(eventId, currentUser.userId())
                .orElseThrow(() -> new RegistrationException(
                        "Регистрация на мероприятие с id %s не найдена".formatted(eventId)));

        var event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException("Мероприятие не найдено"));

        if (event.getStatus() != EventStatus.WAIT_START){
            throw new RegistrationException(
                    "Нельзя отменить регистрацию: мероприятие уже началось или завершено");
        }

        registrationRepository.delete(registration);
        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        eventRepository.save(event);

        log.info("Пользователь {} успешно отменил регистрацию на событие {}",
                currentUser.userId(), eventId);
    }

    public List<Event> getMyRegistrations(TokenPayload currentUser){
        log.info("Запрос списка мероприятий, на которые записан пользователь {}", currentUser.userId());

        return registrationRepository.findAllWithEventByUserId(currentUser.userId()).stream()
                .map(RegistrationEntity::getEvent)
                .map(eventMapper::toDomain)
                .toList();
    }
}
