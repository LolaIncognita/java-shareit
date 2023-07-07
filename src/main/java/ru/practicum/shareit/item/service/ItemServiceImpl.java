package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        userRepository.findUserById(userId);
        return itemRepository.getItemsByUerId(userId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.getItemsByText(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        validationBeforeAdd(userId, itemDto);
        return ItemMapper.toItemDto(itemRepository.addItem(userId, ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = validationBeforeUpdate(userId, itemId, itemDto);
        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }

    private void validationBeforeAdd(long userId, ItemDto itemDto) {
        userRepository.findUserById(userId);
        if (itemDto.getName().isEmpty()) {
            throw new ValidationException("Имя вещи не должно быть пустым.");
        } else if (itemDto.getDescription() == null) {
            throw new ValidationException("Описание вещи не должно быть пустым.");
        } else if (itemDto.getAvailable() == null) {
            throw new ValidationException("Нужно указать, доступна ли вещь.");
        }
    }

    private Item validationBeforeUpdate(long userId, long itemId, ItemDto itemDto) {
        userRepository.findUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        User owner = item.getOwner();
        if (owner.getId() != userId) {
            throw new NotFoundException("Пользователь не является собственником вещи и не имеет прав на её изменение.");
        }
        if (itemDto.getName() != null) {
            if (itemDto.getName().isBlank()) {
                throw new ValidationException("Имя вещи не должно быть пустым.");
            }
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            if (itemDto.getDescription().isBlank()) {
                throw new ValidationException("Описание вещи не должно быть пустым.");
            }
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }
}
