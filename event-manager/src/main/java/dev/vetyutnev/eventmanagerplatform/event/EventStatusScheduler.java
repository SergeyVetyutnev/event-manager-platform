package dev.vetyutnev.eventmanagerplatform.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventRepository eventRepository;

    @Transactional
    @Scheduled(cron = "${app.scheduling.check-statuses-cron}" )
    public void checkAndUpdateEventStatuses(){
        log.debug("Планировщик: проверка статусов мероприятий...");

        List<Long> toStart = eventRepository.findEventsToStart(EventStatus.WAIT_START.name());
        if (!toStart.isEmpty()){
            log.info("Планировщик: запуск {} мероприятий (IDs: {})", toStart.size(), toStart);
            eventRepository.changeStatus(toStart, EventStatus.STARTED);
        }

        List<Long> toFinish = eventRepository.findEventsToFinish(EventStatus.STARTED.name());
        if (!toFinish.isEmpty()){
            log.info("Планировщик: окончание {} мероприятий (IDs: {})", toFinish.size(), toFinish);
            eventRepository.changeStatus(toFinish, EventStatus.FINISHED);
        }
    }
}
