package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {
    private ItemMapper() {
    }

    public static Item convertToEntity(ItemDto itemDto) {
        return Item.builder()
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .build();
    }

    public static ItemDto convertToDto(Item item) {
        return ItemDto.builder()
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }
}
