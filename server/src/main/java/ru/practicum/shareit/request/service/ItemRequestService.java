package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithoutItems;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoWithoutItems createNewItemRequest(long userId, ItemRequestDtoDescription request);

    List<ItemRequestDtoWithItems> getUserItemRequests(long userId);

    List<ItemRequestDtoWithItems> getOtherUsersItemRequests(long userId, int from, int size);

    ItemRequestDtoWithItems getItemRequestById(long userId, long requestId);
}