package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.practicum.shareit.booking.dto.deserialiser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Data
public class BookingDtoRequest {
    private long itemId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime end;
}