package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.validator.DateTimeRange;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@DateTimeRange
public class BookingDtoRequest {
    @NotNull
    private long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
}