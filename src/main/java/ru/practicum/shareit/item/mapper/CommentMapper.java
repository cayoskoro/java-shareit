package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.Collection;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class, imports = LocalDateTime.class)
public interface CommentMapper {
    @Mapping(target = "created", defaultExpression = "java(LocalDateTime.now())")
    Comment convertToEntity(CommentDto dto);

    @Mapping(target = "authorName", source = "entity.author.name")
    CommentDto convertToDto(Comment entity);

    @Mapping(target = "authorName", source = "entity.author.name")
    Collection<CommentDto> convertToDtoCollection(Collection<Comment> entities);
}
