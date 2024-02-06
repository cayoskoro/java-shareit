package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemRequestDtoTest extends ItemBaseTest {
    private final JacksonTester<ItemRequestDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetItemRequestDto() throws Exception {

        log.info("itemRequestDto = {}", itemRequestDto1);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestDto1.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                itemRequestDto1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(
                itemRequestDto1.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(
                itemRequestDto1.getRequestId().intValue());
    }
}