package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.exceptions.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoResponse createBooking(BookingDtoRequest bookingDto, long userId) {
        User booker = checkPresenceAndReturnUserOrElseThrow(userId);
        Item item = checkPresenceAndReturnItemOrElseThrow(bookingDto.getItemId());

        if (!item.getAvailable())
            throw new ItemNotAvailbaleException("Item with id " + bookingDto.getItemId() + " is NOT AVAILABLE");
        if (item.getOwner().getId().equals(booker.getId()))
            throw new BookingNotFoundException("Owner can't book it's own item");

        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDtoResponse updateBooking(long bookingId, Boolean approved, long userId) {
        checkPresenceAndReturnUserOrElseThrow(userId);
        Booking booking = checkPresenceAndReturnBookingOrElseThrow(bookingId);

        if (booking.getItem().getOwner().getId() != userId)
            throw new AccessDenyException("User with id " + userId + " is not the owner of item");

        if ((String.valueOf(booking.getStatus()).equals("APPROVED") && approved)
                || (String.valueOf(booking.getStatus()).equals("REJECTED") && !approved))
            throw new UnsupportedStateException("Booking status has already been changed.");


        bookingRepository.updateBookingStatusById(bookingId, approved);
        BookingDtoResponse dto = BookingMapper.toBookingDto((Objects.requireNonNull(bookingRepository.findById(bookingId).orElse(null))));
        if (approved) dto.setStatus(String.valueOf(BookingStatus.APPROVED));
        else dto.setStatus(String.valueOf(BookingStatus.REJECTED));
        return dto;
    }

    @Override
    public BookingDtoResponse getBookingById(long bookingId, long userId) {
        checkPresenceAndReturnUserOrElseThrow(userId);
        Booking booking = checkPresenceAndReturnBookingOrElseThrow(bookingId);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw new AccessDenyException("User with id " + userId + " is not the owner / booker of item");

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUserByState(String state, long userId) {
        checkPresenceAndReturnUserOrElseThrow(userId);

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdCurrent(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdPast(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdFuture(userId);
                break;
            case "WAITING":
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state));
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return BookingMapper.toBookingDto(bookings);
    }

    @Override
    public List<BookingDtoResponse> getOwnerItemsBooked(String state, long userId) {
        checkPresenceAndReturnUserOrElseThrow(userId);

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItemOwnerIdCurrentOrderByStartDesc(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdFutureOrderByStartDesc(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdPastOrderByStartDesc(userId);
                break;
            case "WAITING":
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state));
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return BookingMapper.toBookingDto(bookings);
    }

    private User checkPresenceAndReturnUserOrElseThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    private Item checkPresenceAndReturnItemOrElseThrow(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(("Item with id " + itemId + " not found")));
    }

    private Booking checkPresenceAndReturnBookingOrElseThrow(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + " not found"));
    }
}