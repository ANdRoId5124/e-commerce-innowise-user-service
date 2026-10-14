package com.andrei.ecommerce_innowise.mappers;

import com.andrei.ecommerce_innowise.DTO.request.user.CreateUserRequest;
import com.andrei.ecommerce_innowise.DTO.request.user.UpdateUserRequest;
import com.andrei.ecommerce_innowise.DTO.response.user.UserResponse;
import com.andrei.ecommerce_innowise.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;

@Mapper
public interface UserMapper {
  UserMapper Instance = Mappers.getMapper(UserMapper.class);

  User userDTOToUser(CreateUserRequest userDto);

  UserResponse userToUserResponseDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy =
      NullValuePropertyMappingStrategy.IGNORE
  )
  User updateUserFromUserUpdateRequest(
      UpdateUserRequest userUpdateRequest,
      @MappingTarget User user)
      ;

}
