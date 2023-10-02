package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithoutItems;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoWithoutItems createItemRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemRequestDtoDescription request
    ) {
        log.info("POST request received for Item Request: {}", request);
        ItemRequestDtoWithoutItems response = itemRequestService.createNewItemRequest(userId, request);
        log.info("Item Request created: {}", response);
        return response;
    }

    @GetMapping
    public List<ItemRequestDtoWithItems> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("GET request received for all Item Request of user: {}", userId);
        List<ItemRequestDtoWithItems> response = itemRequestService.getUserItemRequests(userId);
        log.info("Item requests of user {}: {}", userId, response);
        return response;
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("GET request received for other Item Request");
        List<ItemRequestDtoWithItems> response = itemRequestService.getOtherUsersItemRequests(userId, from, size);
        log.info("Other item requests: {}", response);
        return response;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItems getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable(name = "requestId") long requestId
    ) {
        log.info("GET request received for Item Request: {}", requestId);
        ItemRequestDtoWithItems response = itemRequestService.getItemRequestById(userId, requestId);
        log.info("Item request {}: {}", requestId, response);
        return response;
    }
}