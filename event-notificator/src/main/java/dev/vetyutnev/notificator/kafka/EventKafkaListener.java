package dev.vetyutnev.notificator.kafka;

import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventKafkaListener {

    private final NotificationProcessingService notificationProcessingService;

    @KafkaListener(
            topics = "${app.kafka.topics.event-changes}",
            groupId = "event-notificator-group"
    )
    public void listenEventChanges(EventChangeKafkaMessage message){
        log.info("Получено сообщение из Kafka: eventId={}, eventType={}, messageId={}",
                message.eventId(), message.eventType(), message.messageId());

        try {
            notificationProcessingService.processMessage(message);
            log.debug("Сообщение {} успешно обработано", message.messageId());
        } catch (Exception e) {
            log.error("Ошибка при обработке Kafka сообщения (messageId={}): {}",
                    message.messageId(), e.getMessage(), e);
        }
    }
}
