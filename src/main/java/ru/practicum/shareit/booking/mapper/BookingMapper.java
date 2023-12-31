package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingClosest;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static BookingDtoResponse toBookingDto(Booking booking) {
        String startDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(booking.getStart());

        String endDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(booking.getEnd());

        return BookingDtoResponse.builder()
                .id(booking.getId())
                .status(String.valueOf(booking.getStatus()))
                .start(startDate)
                .end(endDate)
                .booker(booking.getBooker() != null ? UserMapper.toUserDto(booking.getBooker()) : null)
                .item(ItemMapper.toItemDto(booking.getItem()))
                .build();
    }

    public static List<BookingDtoResponse> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static Booking toBooking(BookingDtoRequest dto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static BookingDtoForOwner toBookingDtoForOwner(Booking booking) {
        if (booking == null) {
            return null;
        } else {
            return new BookingDtoForOwner(booking.getId(), booking.getBooker().getId(), booking.getStart(),
                    booking.getEnd());
        }
    }

    public static BookingClosest toBookingClosestFromBookingDtoForOwner(BookingDtoForOwner bookingDtoForOwner) {
        if (bookingDtoForOwner == null) {
            return null;
        } else {
            return new BookingClosest(bookingDtoForOwner.getId(), bookingDtoForOwner.getBookerId());
        }
    }

    public static BookingClosest toBookingClosestFromBooking(Booking booking) {
        if (booking == null) {
            return null;
        } else {
            return new BookingClosest(booking.getId(), booking.getBooker().getId());
        }
    }
}