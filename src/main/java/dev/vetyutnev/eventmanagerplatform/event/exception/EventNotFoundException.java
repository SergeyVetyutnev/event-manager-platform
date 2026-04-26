package dev.vetyutnev.eventmanagerplatform.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
