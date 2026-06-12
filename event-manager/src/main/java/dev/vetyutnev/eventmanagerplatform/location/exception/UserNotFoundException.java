package dev.vetyutnev.eventmanagerplatform.location.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
