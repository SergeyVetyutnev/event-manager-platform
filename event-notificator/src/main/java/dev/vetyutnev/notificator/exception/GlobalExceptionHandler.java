package dev.vetyutnev.notificator.exception;

import dev.vetyutnev.eventmanagerplatform.common.exception.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Ошибка чтения JSON: {}", ex.getMessage());

        var response = new ErrorMessageResponse(
                "Некорректный запрос",
                "Ошибка структуры JSON или передано неизвестное поле. Проверьте запрос.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleAllExceptions(Exception e) {

        String incidentId = UUID.randomUUID().toString();
        log.error("Внутренняя ошибка сервера [Incident ID: {}]: ", incidentId, e);

        var response = new ErrorMessageResponse(
                "Внутренняя ошибка сервера",
                "ID ошибки: %s".formatted(incidentId),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
