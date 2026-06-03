package dev.vetyutnev.eventmanagerplatform.event.kafka;

import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Value("${app.kafka.topics.event-changes}")
    private String topicName;

    public void publishEventChange(EventChangeKafkaMessage message) {
        log.info("Отправка сообщения об изменении события {} в Kafka", message.eventId());

        kafkaTemplate.send(topicName, String.valueOf(message.eventId()), message);
    }
}
