package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private BookingStatus status;
}
