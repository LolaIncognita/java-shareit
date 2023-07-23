package ru.practicum.shareit.exceptions;

public class EmailValidationException extends RuntimeException {
    public EmailValidationException(String message) {
        super(message);
    }
}
