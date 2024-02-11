package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class, imports = LocalDateTime.class)
public interface ItemRequestMapper {
    @Mapping(target = "created", defaultExpression = "java(LocalDateTime.now())")
    ItemRequest convertToEntity(ItemRequestDto dto);

    ItemRequestDto convertToDto(ItemRequest entity);
}
