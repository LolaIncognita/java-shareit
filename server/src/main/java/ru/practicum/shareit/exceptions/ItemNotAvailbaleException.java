package ru.practicum.shareit.exceptions;

public class ItemNotAvailbaleException extends RuntimeException {
    public ItemNotAvailbaleException(String message) {
        super(message);
    }
}