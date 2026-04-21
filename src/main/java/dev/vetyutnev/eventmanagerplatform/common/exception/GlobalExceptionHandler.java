package dev.vetyutnev.eventmanagerplatform.common.exception;

import dev.vetyutnev.eventmanagerplatform.location.exception.LocationNotFoundException;
import dev.vetyutnev.eventmanagerplatform.location.exception.UserNotFoundException;
import dev.vetyutnev.eventmanagerplatform.security.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(LocationNotFoundException e){
        log.warn("Not found error: {}", e.getMessage());

        var response = new ErrorMessageResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(UserNotFoundException e){
        log.warn("Not found error: {}", e.getMessage());

        var response = new ErrorMessageResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleArgumentNotValidException(MethodArgumentNotValidException e){
        var detailedMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation error: {}", detailedMessage);

        var response = new ErrorMessageResponse(
                "Некорректный запрос",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ErrorMessageResponse>
    handleInvalidCredentialException(InvalidCredentialException e){
        log.warn("Auth error: {}", e.getMessage());
        var response = new ErrorMessageResponse(
                "необходима аутентификация",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
