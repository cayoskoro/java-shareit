package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto dto, @MappingTarget User entity);

    User convertToEntity(UserDto dto);

    UserDto convertToDto(User entity);

    User clone(User entity);

    Collection<UserDto> convertToDtoCollection(Collection<User> entities);
}
