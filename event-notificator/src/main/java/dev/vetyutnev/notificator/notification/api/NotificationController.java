package dev.vetyutnev.notificator.notification.api;

import dev.vetyutnev.eventmanagerplatform.common.security.TokenPayload;
import dev.vetyutnev.notificator.notification.api.dto.MarkNotificationsAsReadRequest;
import dev.vetyutnev.notificator.notification.mapper.NotificationMapper;
import dev.vetyutnev.notificator.notification.api.dto.NotificationResponseDto;
import dev.vetyutnev.notificator.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@AuthenticationPrincipal TokenPayload payload){
        log.info("HTTP GET /notifications - запрос непрочитанных уведомлений");

        List<NotificationResponseDto> response =
                notificationService.getUnreadNotifications(payload).stream()
                        .map(notificationMapper::toDto)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> markAsRead (
            @Valid @RequestBody MarkNotificationsAsReadRequest request,
            @AuthenticationPrincipal TokenPayload payload
    ) {
        log.info("HTTP POST /notifications - пометка уведомлений как прочитанных");

        notificationService.markAsRead(request.notificationIds(), payload);

        return ResponseEntity.noContent().build();
    }

}
