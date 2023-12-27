package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface ItemMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateItemFromItemDto(ItemDto dto, @MappingTarget Item item);

    Item convertToEntity(ItemDto dto);

    ItemDto convertToDto(Item entity);

    Item clone(Item entity);
}
