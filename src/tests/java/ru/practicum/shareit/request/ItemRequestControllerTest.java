package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithoutItems;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.CrudOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getUserItemRequests() {
        long userId = createUser(UserDto.builder().name("Mark").email("mark@email.com").build()).getId();
        ItemRequestDtoDescription itemRequestDescription = new ItemRequestDtoDescription();
        itemRequestDescription.setDescription("description");

        ItemRequestDtoWithoutItems itemRequestInfo = createItemRequest(itemRequestDescription, userId);
        ItemRequestDtoWithItems itemRequestInfoWithItems = ItemRequestDtoWithItems.builder()
                .id(itemRequestInfo.getId())
                .description(itemRequestInfo.getDescription())
                .created(itemRequestInfo.getCreated())
                .items(Collections.emptyList())
                .build();

        List<ItemRequestDtoWithItems> expected = new ArrayList<>(
                List.of(itemRequestInfoWithItems)
        );

        MvcResult result = mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<ItemRequestDtoWithItems> actual = List.of(
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        ItemRequestDtoWithItems[].class
                )
        );

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    void getOtherUsersItemRequests() {
        long userId = createUser(UserDto.builder().name("Mark").email("mark@email.com").build()).getId();
        long otherId = createUser(UserDto.builder().name("Toma").email("Toma@email.com").build()).getId();
        ItemRequestDtoDescription itemRequestDescription = new ItemRequestDtoDescription();
        itemRequestDescription.setDescription("description");

        ItemRequestDtoWithoutItems itemRequestInfo = createItemRequest(itemRequestDescription, userId);
        ItemRequestDtoWithItems itemRequestInfoWithItems = ItemRequestDtoWithItems.builder()
                .id(itemRequestInfo.getId())
                .description(itemRequestInfo.getDescription())
                .created(itemRequestInfo.getCreated())
                .items(Collections.emptyList())
                .build();

        List<ItemRequestDtoWithItems> expected = new ArrayList<>(
                List.of(itemRequestInfoWithItems)
        );

        MvcResult result = mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(otherId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<ItemRequestDtoWithItems> actual = List.of(
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        ItemRequestDtoWithItems[].class
                )
        );

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    void getItemRequestById() {
        long userId = createUser(UserDto.builder().name("Mark").email("mark@email.com").build()).getId();
        ItemRequestDtoDescription itemRequestDescription = new ItemRequestDtoDescription();
        itemRequestDescription.setDescription("description");

        ItemRequestDtoWithoutItems itemRequestInfo = createItemRequest(itemRequestDescription, userId);
        ItemRequestDtoWithItems expected = ItemRequestDtoWithItems.builder()
                .id(itemRequestInfo.getId())
                .description(itemRequestInfo.getDescription())
                .created(itemRequestInfo.getCreated())
                .items(Collections.emptyList())
                .build();

        MvcResult result = mockMvc.perform(get("/requests/{requestId}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ItemRequestDtoWithItems actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ItemRequestDtoWithItems.class
        );

        assertEquals(expected, actual);
    }
}