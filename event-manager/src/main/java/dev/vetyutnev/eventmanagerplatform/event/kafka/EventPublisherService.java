package dev.vetyutnev.eventmanagerplatform.event.kafka;

import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import dev.vetyutnev.eventmanagerplatform.event.outbox.repository.OutBoxMessageRepository;
import dev.vetyutnev.eventmanagerplatform.event.outbox.repository.OutboxMessageEntity;
import dev.vetyutnev.eventmanagerplatform.event.outbox.domain.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final OutBoxMessageRepository outBoxMessageRepository;
    private final ObjectMapper objectMapper;

    public void publishEventChange(EventChangeKafkaMessage message) {
        log.info("Сохранение Kafka сообщения в Outbox для события {}", message.eventId());

        String payloadJson;

        try {
            payloadJson = objectMapper.writeValueAsString(message);
        }catch (JacksonException e){
            log.error("Ошибка сериализации сообщения для Outbox (eventId={})", message.eventId(), e);

            throw new RuntimeException("Не удалось сериализовать Kafka-сообщение", e);
        }

        OutboxMessageEntity outboxMessage = OutboxMessageEntity.builder()
                .eventId(message.eventId())
                .payloadJson(payloadJson)
                .status(OutboxStatus.PENDING)
                .createdAt(OffsetDateTime.now())
                .build();

        outBoxMessageRepository.save(outboxMessage);
    }
}
