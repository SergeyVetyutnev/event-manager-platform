package dev.vetyutnev.notificator.notification.service;

import dev.vetyutnev.notificator.notification.repository.NotificationEntityRepository;
import dev.vetyutnev.notificator.notification.repository.NotificationEventPayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

    private final NotificationEntityRepository notificationEntityRepository;
    private final NotificationEventPayloadRepository notificationEventPayloadRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUpNotifications(){
        log.info("Запуск отчистки старых уведомлений...");

        var cutoffDate = OffsetDateTime.now().minusDays(7);

        int deletedNotificationsCount = notificationEntityRepository.deleteOldNotifications(cutoffDate);
        log.info("Удалено {} старых пользовательских уведомлений.", deletedNotificationsCount);

        int deletedPayloadsCount = notificationEventPayloadRepository.deleteOrphanedPayloads(cutoffDate);
        log.info("Удалено {} старых orphaned payload'ов.", deletedPayloadsCount);



    }
}
