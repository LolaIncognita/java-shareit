package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithoutItems;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest fromItemRequestDto(ItemRequestDtoDescription itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestDtoWithoutItems toItemRequestDto(ItemRequest itemRequest) {
        String created = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(itemRequest.getCreated());

        return ItemRequestDtoWithoutItems.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(created)
                .build();
    }

    public static ItemRequestDtoWithItems toItemRequestWithItemsDto(ItemRequest itemRequest) {
        String created = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(itemRequest.getCreated());

        return ItemRequestDtoWithItems.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(created)
                .build();
    }
}