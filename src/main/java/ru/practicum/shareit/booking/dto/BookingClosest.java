package ru.practicum.shareit.booking.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingClosest {
    private Long id;
    private Long bookerId;
}