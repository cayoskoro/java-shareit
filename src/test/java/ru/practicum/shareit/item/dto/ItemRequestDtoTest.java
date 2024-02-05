package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void shouldGetItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .requestId(1L)
                .build();

        log.info("itemRequestDto = {}", itemRequestDto);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(
                itemRequestDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(
                itemRequestDto.getRequestId().intValue());
    }
}