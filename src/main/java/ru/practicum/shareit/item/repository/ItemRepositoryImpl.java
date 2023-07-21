package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long,List<Long>> listItemsIdByUer = new HashMap<>();

    private long generteId = 1;

    @Override
    public Item getItemById(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с id = " + itemId + " отсутствует в базе данных.");
        }
        return items.get(itemId);
    }


    @Override
    public List<Item> getItemsByUserId(long userId) {
        if (!listItemsIdByUer.containsKey(userId)) {
            throw new NotFoundException("У пользователя с id = {} " + userId + " отсутствует вещи в базе данных.");
        }
        return listItemsIdByUer.get(userId).stream().map(items::get).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsByText(String text) {
        List<Item> list = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.isAvailable()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    @Override
    public Item addItem(long userId, Item item) {
        item.setId(generteId++);
        User owner = User.builder().id(userId).build();
        item.setOwner(owner);
        items.put(item.getId(), item);
        listItemsIdByUer.computeIfAbsent(userId, k -> new ArrayList<>()).add(item.getId());
        log.info("Вещь с id = {} добавлена.", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Вещь с id = {} обновлена.", item.getId());
        return item;
    }
}