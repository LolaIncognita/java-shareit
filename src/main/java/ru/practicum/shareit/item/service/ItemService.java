package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long ownerId, ItemDto itemDto);

    ItemDto getItemByOwnerId(long itemId, long ownerId);

    List<ItemDto> getAllItemsByOwnerId(long ownerId);

    ItemDto updateItem(long itemId, long ownerId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    void deleteItem(long itemId);

    CommentResponseDto addComment(CommentRequestDto commentRequestDto, long bookerId, long itemId);
}