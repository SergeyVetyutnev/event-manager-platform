package dev.vetyutnev.notificator.notification;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;
import dev.vetyutnev.eventmanagerplatform.common.security.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationEntityRepository notificationEntityRepository;
    private final ObjectMapper objectMapper;

    public List<Notification> getUnreadNotifications(TokenPayload currentUser) {

        List<NotificationEntity> entities =
                notificationEntityRepository.findAllByUserIdAndIsReadFalse(currentUser.userId());

        return entities.stream().map(entity -> {
            var payloadEntity = entity.getPayload();
            String json = payloadEntity.getPayloadJson();

            List<ChangeItem> changes = Collections.emptyList();

                if (payloadEntity != null && !json.isBlank()){
                    try {
                        changes = objectMapper.readValue(json, new TypeReference<List<ChangeItem>>() {});
                    } catch (JacksonException e) {
                        log.error("Ошибка парсинга JSON для payloadId = {}", payloadEntity.getId(), e);
                    }
                }

                var payloadDomain = new NotificationPayload(
                        payloadEntity.getMessageId(),
                        payloadEntity.getEventType(),
                        payloadEntity.getEventId(),
                        payloadEntity.getOccurredAt(),
                        payloadEntity.getChangedById(),
                        payloadEntity.getOwnerId(),
                        payloadEntity.getEventName(),
                        changes
                );

                String humanMessage = switch (payloadEntity.getEventType()){
                    case "EVENT_UPDATED" -> "Мероприятие было изменено";
                    case "EVENT_CANCELLED" -> "Мероприятие было отменено";
                    case "EVENT_STARTED" -> "Мероприятие началось!";
                    case "EVENT_FINISHED" -> "Мероприятие завершено";
                    default -> "Новое уведомление о мероприятии";
                };

                return new Notification (
                        entity.getId(),
                        entity.getUserId(),
                        payloadEntity.getEventType(),
                        payloadEntity.getEventId(),
                        entity.getCreatedAt(),
                        entity.isRead(),
                        humanMessage,
                        payloadDomain
                );
        }).toList();
    }

    @Transactional
    public void markAsRead(List<Long> notificationIds, TokenPayload currentUser) {
        log.info("Пользователь {} отмечает уведомления {} как прочитанные",
                currentUser.userId(), notificationIds);

        if (notificationIds == null || notificationIds.isEmpty()) {
            return;
        }

        notificationEntityRepository.markAsRead(notificationIds, currentUser.userId());


    }
}
