package ru.practicum.shareit.exceptions;

public class CommentNotAuthorisedException extends RuntimeException {
    public CommentNotAuthorisedException(String message) {
        super(message);
    }
}