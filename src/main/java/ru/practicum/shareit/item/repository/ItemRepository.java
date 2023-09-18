package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long ownerId);

    @Modifying
    @Query("UPDATE Item e SET " +
            "e.name = CASE WHEN :#{#item.name} IS NOT NULL THEN :#{#item.name} ELSE e.name END, " +
            "e.description = CASE WHEN :#{#item.description} IS NOT NULL THEN :#{#item.description} ELSE e.description END, " +
            "e.available = CASE WHEN :#{#item.available} IS NOT NULL THEN :#{#item.available} ELSE e.available END " +
            "WHERE e.id = :itemId AND e.owner.id = :ownerId")
    void updateItemFields(Item item, Long ownerId, Long itemId);

    @Query("SELECT it FROM Item it " +
            "WHERE (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(it.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND it.available = true ")
    List<Item> searchItemByNameOrDescription(String text);
}