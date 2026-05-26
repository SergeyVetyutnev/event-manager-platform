package dev.vetyutnev.notificator.kafka;

import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import dev.vetyutnev.notificator.notification.NotificationEntity;
import dev.vetyutnev.notificator.notification.NotificationEntityRepository;
import dev.vetyutnev.notificator.notification.NotificationEventPayloadEntity;
import dev.vetyutnev.notificator.notification.NotificationEventPayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationProcessingService {

    private final ObjectMapper objectMapper;
    private final NotificationEventPayloadRepository notificationEventPayloadRepository;
    private final NotificationEntityRepository notificationEntityRepository;

    @Transactional
    @SneakyThrows
    public void processMessage(EventChangeKafkaMessage message){

        if (notificationEventPayloadRepository.existsByMessageId(message.messageId())){
            log.warn("Сообщение уже было обработано ранее (messageId={})", message.messageId());
            return;
        }

        String payloadJson = objectMapper.writeValueAsString(message.changes());

        var notificationEventPayloadEntity = NotificationEventPayloadEntity.builder()
                .messageId(message.messageId())
                .eventType(message.eventType())
                .eventId(message.eventId())
                .occurredAt(message.occurredAt())
                .changedById(message.changedById())
                .ownerId(message.ownerId())
                .payloadJson(payloadJson)
                .build();

        notificationEventPayloadEntity = notificationEventPayloadRepository.save(notificationEventPayloadEntity);

        List<Long> subscribers = message.subscribers();
        if (subscribers != null && !subscribers.isEmpty()) {

            List<NotificationEntity> notifications = new ArrayList<>();
            for (Long subscriber : subscribers) {
                var notificationEntity = NotificationEntity.builder()
                        .userId(subscriber)
                        .payload(notificationEventPayloadEntity)
                        .isRead(false)
                        .createdAt(OffsetDateTime.now())
                        .build();
                notifications.add(notificationEntity);
            }

            notificationEntityRepository.saveAll(notifications);

            log.info("Сообщение успешно обработано (messageId={}, subscribersCount={})",
                    message.messageId(), message.subscribers().size());
        } else {
            log.info("Сообщение обработано, но у события нет подписчиков (messageId={})",
                    message.messageId());
        }
    }
}
