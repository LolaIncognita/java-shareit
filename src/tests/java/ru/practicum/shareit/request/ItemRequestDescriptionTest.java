package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class ItemRequestDescriptionTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator;

    public ItemRequestDescriptionTest() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    @SneakyThrows
    public void testSerialize() {
        ItemRequestDtoDescription itemRequestDescription = new ItemRequestDtoDescription();
        itemRequestDescription.setDescription("Item description");

        String json = objectMapper.writeValueAsString(itemRequestDescription);

        String expectedJson = "{\"description\":\"Item description\"}";
        assertEquals(expectedJson, json);
    }

    @Test
    @SneakyThrows
    public void testDeserialize() {
        String json = "{\"description\":\"Item description\"}";

        ItemRequestDtoDescription itemRequestDescription = objectMapper.readValue(json, ItemRequestDtoDescription.class);

        assertEquals("Item description", itemRequestDescription.getDescription());
    }

    @Test
    public void testValidation() {
        ItemRequestDtoDescription itemRequestDescription = new ItemRequestDtoDescription();
        itemRequestDescription.setDescription("");

        Set<ConstraintViolation<ItemRequestDtoDescription>> violations = validator.validate(itemRequestDescription);
        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDtoDescription> notEmptyViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("description"))
                .filter(v -> v.getMessage().equals("не должно быть пустым"))
                .findFirst()
                .orElse(null);
        assertNotNull(notEmptyViolation);
    }
}