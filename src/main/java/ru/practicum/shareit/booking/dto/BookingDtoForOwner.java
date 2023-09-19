package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDtoForOwner {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}