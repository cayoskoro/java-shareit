package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface ItemMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateItemFromItemRequestDto(ItemRequestDto dto, @MappingTarget Item item);

    Item convertRequestDtoToEntity(ItemRequestDto dto);

    @Mapping(target = "requestId", source = "entity.request.id")
    ItemResponseDto convertToResponseDto(Item entity);

    Item clone(Item entity);
}
