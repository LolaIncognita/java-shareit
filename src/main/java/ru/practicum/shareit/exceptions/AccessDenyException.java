package ru.practicum.shareit.exceptions;

public class AccessDenyException extends RuntimeException {
    public AccessDenyException(String message) {
        super(message);
    }
}