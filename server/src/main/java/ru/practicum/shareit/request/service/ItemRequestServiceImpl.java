package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemResponseForRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithoutItems;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.exceptions.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDtoWithoutItems createNewItemRequest(long userId, ItemRequestDtoDescription request) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ItemRequest itemRequest = ItemRequestMapper.fromItemRequestDto(request);
            itemRequest.setRequester(user);
            itemRequest.setCreated(ZonedDateTime.now());
            itemRequest = itemRequestRepository.save(itemRequest);
            return ItemRequestMapper.toItemRequestDto(itemRequest);
        } else {
            throw new UserNotFoundException("User with id " + userId + " doesn't exist.");
        }
    }

    @Override
    public List<ItemRequestDtoWithItems> getUserItemRequests(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            List<ItemRequestDtoWithItems> requests = itemRequestRepository
                    .findAllByRequesterIdOrderByCreatedDesc(userId)
                    .stream()
                    .map(ItemRequestMapper::toItemRequestWithItemsDto)
                    .collect(Collectors.toList());

            return addItemsInItemRequest(requests);
        } else {
            throw new UserNotFoundException("User with id " + userId + " was not found");
        }
    }

    @Override
    public List<ItemRequestDtoWithItems> getOtherUsersItemRequests(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequestDtoWithItems> requests = itemRequestRepository
                .findByRequesterIdIsNot(userId, page)
                .stream()
                .map(ItemRequestMapper::toItemRequestWithItemsDto)
                .collect(Collectors.toList());

        return addItemsInItemRequest(requests);
    }

    @Override
    public ItemRequestDtoWithItems getItemRequestById(long userId, long requestId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        Optional<ItemRequest> maybeItemRequest = itemRequestRepository.findById(requestId);
        if (maybeItemRequest.isPresent()) {
            ItemRequest itemRequest = maybeItemRequest.get();
            ItemRequestDtoWithItems response = ItemRequestMapper.toItemRequestWithItemsDto(itemRequest);
            List<ItemResponseForRequest> items = getItemResponsesForRequest(requestId);
            response.setItems(items);
            return response;
        } else {
            throw new ItemRequestNotFoundException("Item request " + requestId + " not found");
        }
    }

    private List<ItemResponseForRequest> getItemResponsesForRequest(long requestId) {
        return itemRepository.getItemDescriptionForRequest(requestId);
    }

    private List<ItemRequestDtoWithItems> addItemsInItemRequest(List<ItemRequestDtoWithItems> itemRequestDtoWithItems) {
        List<Long> itemRequestIds = itemRequestDtoWithItems.stream().map(ItemRequestDtoWithItems::getId)
                .collect(Collectors.toList());
        List<ItemResponseForRequest> itemsDto = itemRepository.findAllByRequestIdIn(itemRequestIds).stream().map(ItemMapper::toItemResponseForRequest)
                .collect(Collectors.toList());
        for (ItemRequestDtoWithItems itemRequestDto : itemRequestDtoWithItems) {
            itemRequestDto.setItems(itemsDto.stream().filter(i -> Objects.equals(i.getRequestId(), itemRequestDto.getId()))
                    .collect(Collectors.toList()));
        }
        return itemRequestDtoWithItems;
    }
}