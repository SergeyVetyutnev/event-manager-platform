package dev.vetyutnev.eventmanagerplatform.event.outbox.service;

import dev.vetyutnev.eventmanagerplatform.common.kafka.EventChangeKafkaMessage;
import dev.vetyutnev.eventmanagerplatform.event.outbox.domain.OutboxStatus;
import dev.vetyutnev.eventmanagerplatform.event.outbox.repository.OutBoxMessageRepository;
import dev.vetyutnev.eventmanagerplatform.event.outbox.repository.OutboxMessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxRelayScheduler {

    private final OutBoxMessageRepository outBoxMessageRepository;
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.event-changes}")
    private String topicName;

    @Scheduled(fixedDelay = 2000)
    public void processOutboxMessages(){
        List<OutboxMessageEntity> pendingMessages = outBoxMessageRepository.
                findAllByStatusIsOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (pendingMessages.isEmpty()){
            return;
        }

        log.debug("Найдено сообщения для отправки {}", pendingMessages.size());

        for (OutboxMessageEntity entity : pendingMessages){
            try {
                EventChangeKafkaMessage kafkaMessage = objectMapper.readValue(
                        entity.getPayloadJson(), EventChangeKafkaMessage.class
                );

                kafkaTemplate.send(topicName, String.valueOf(entity.getEventId()), kafkaMessage);

                entity.setStatus(OutboxStatus.SENT);
                outBoxMessageRepository.save(entity);

                log.info("Outbox: Сообщение для события {} успешно отправлено", entity.getEventId());

            } catch (JacksonException e) {
                log.error("Ошибка парсинга JSON для ID {}, помечено как FAILED", entity.getId());

                entity.setStatus(OutboxStatus.FAILED);
                outBoxMessageRepository.save(entity);
            } catch (Exception e) {
                log.error("Outbox: Сетевая ошибка Kafka при отправке ID {}: {}", entity.getId(), e.getMessage());
                break;
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void deleteOldOutboxMessages(){
        log.info("Outbox: Запуск очистки старых сообщений...");

        int deletedMessages = outBoxMessageRepository.deleteOldMessages(
                List.of(OutboxStatus.SENT),
                OffsetDateTime.now().minusDays(1)
        );

        log.info("Outbox: Очистка завершена. Удалено {} старых сообщений.", deletedMessages);
    }
}
