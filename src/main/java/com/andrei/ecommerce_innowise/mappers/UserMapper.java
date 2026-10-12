package com.andrei.ecommerce_innowise.mappers;

import com.andrei.ecommerce_innowise.DTO.UserDto;
import com.andrei.ecommerce_innowise.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy =
    NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  UserMapper Instance = Mappers.getMapper(UserMapper.class);

  UserDto userToUserDTO(User user);

  User userDTOToUser(UserDto userDto);

  User updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

}
