package dev.vetyutnev.notificator.notification.service;

import dev.vetyutnev.notificator.notification.repository.NotificationEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCountService {

    private final StringRedisTemplate redisTemplate;
    private final NotificationEntityRepository notificationEntityRepository;

    public void incrementUnread(Long userId){

        var key = "notif:unread:" + userId;

        try {
            redisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            log.warn("Redis недоступен при инкременте (key={}): {}", key, e.getMessage());
        }
    }

    public void syncUnreadFromDatabase(Long userId){

        var key = "notif:unread:" + userId;
        var count = String.valueOf(notificationEntityRepository.countByUserIdAndIsReadFalse(userId));

        try {
            redisTemplate.opsForValue().set(key, count);
        } catch (Exception e) {
            log.warn("Redis недоступен при синхронизации (key={}, count={}): {}", key, count, e.getMessage());
        }
    }
}
