package dev.vetyutnev.eventmanagerplatform.event;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;
import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import dev.vetyutnev.eventmanagerplatform.event.kafka.EventPublisherService;
import dev.vetyutnev.eventmanagerplatform.event.registration.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventPublisherService eventPublisherService;
    private final CacheManager cacheManager;

    @Transactional
    @Scheduled(cron = "${app.scheduling.check-statuses-cron}" )
    public void checkAndUpdateEventStatuses(){
        log.debug("Планировщик: проверка статусов мероприятий...");

        List<Long> toStart = eventRepository.findEventsToStart(EventStatus.WAIT_START.name());
        if (!toStart.isEmpty()){
            log.info("Планировщик: запуск {} мероприятий (IDs: {})", toStart.size(), toStart);
            eventRepository.changeStatus(toStart, EventStatus.STARTED);

            publishStatusChanges(toStart, EventStatus.WAIT_START, EventStatus.STARTED);
        }

        List<Long> toFinish = eventRepository.findEventsToFinish(EventStatus.STARTED.name());
        if (!toFinish.isEmpty()){
            log.info("Планировщик: окончание {} мероприятий (IDs: {})", toFinish.size(), toFinish);
            eventRepository.changeStatus(toFinish, EventStatus.FINISHED);

            publishStatusChanges(toFinish, EventStatus.STARTED, EventStatus.FINISHED);
        }
    }

    private void publishStatusChanges(List<Long> eventIds, EventStatus oldStatus, EventStatus newStatus){

        List<EventEntity> events = eventRepository.findAllById(eventIds);

        var eventCache = cacheManager.getCache("events");

        for (EventEntity event : events){

            if(eventCache != null){
                eventCache.evict("id:" + event.getId());
            }

            List<Long> subscribers = registrationRepository.findUserIdsByEventId(event.getId());

            var changeItem = ChangeItem.builder()
                    .field("status")
                    .oldValue(oldStatus.name())
                    .newValue(newStatus.name())
                    .build();

            var message = EventChangeKafkaMessage.builder()
                    .messageId(UUID.randomUUID())
                    .eventType("EVENT_" + newStatus.name())
                    .eventId(event.getId())
                    .occurredAt(OffsetDateTime.now())
                    .ownerId(event.getOwnerId())
                    .changedById(null)
                    .eventName(event.getName())
                    .subscribers(subscribers)
                    .changes(List.of(changeItem))
                    .build();

            eventPublisherService.publishEventChange(message);
        }
    }
}
