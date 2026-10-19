package com.andrei.ecommerce_innowise.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.andrei.ecommerce_innowise.DTO.request.user.CreateUserRequest;
import com.andrei.ecommerce_innowise.DTO.request.user.UpdateUserRequest;
import com.andrei.ecommerce_innowise.DTO.response.user.UserResponse;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.exception.user.UserNotFoundException;
import com.andrei.ecommerce_innowise.mappers.UserMapper;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserService userService;

  @Test
  void UserService_CreateUser_ShouldCreateUserAndReturnUserResponse(){
    CreateUserRequest request = CreateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User user = User.builder().build();
    User savedUser = User.builder().id(1L).build();
    UserResponse expectedRepsonse = UserResponse.builder().id(1L).build();


    when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
    when(mapper.userDTOToUser(request)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(savedUser);
    when(mapper.userToUserResponseDto(savedUser)).thenReturn(expectedRepsonse);

    UserResponse result = userService.createUser(request);

    assertEquals(1L, result.getId());
    verify(mapper).userDTOToUser(request);
    verify(userRepository).save(user);
  }

  @Test
  void UserService_CreateUser_ShouldThrowIllegalArgumentException(){
    CreateUserRequest request = CreateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User existingUser = User.builder().build();

    when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

    assertThrows(IllegalArgumentException.class,
        () -> userService.createUser(request));

    verify(userRepository, never()).save(any(User.class));
  }


  @Test
  void UserService_UpdateUser_ShouldUpdateUserAndReturnUserResponse(){
    UpdateUserRequest request = UpdateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    Long updatedUserId = 1L;

    User existingUser = User.builder().id(updatedUserId).build();
    User updatedUser = User.builder().id(updatedUserId).build();

    UserResponse expectedResponse = UserResponse.builder().id(1L).build();


    when(userRepository.findById(updatedUserId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(updatedUser)).thenReturn(updatedUser);
    when(mapper.updateUserFromUserUpdateRequest(request, existingUser)).thenReturn(updatedUser);
    when(mapper.userToUserResponseDto(updatedUser)).thenReturn(expectedResponse);

    UserResponse result = userService.updateUser(request, updatedUserId);

    assertNotNull(request);
    assertEquals(result.getId(), expectedResponse.getId());

    verify(userRepository).findById(updatedUserId);
    verify(userRepository).save(updatedUser);
    verify(mapper).userToUserResponseDto(updatedUser);
  }

  @Test
  void UserService_UpdateUser_ShouldReturnUserNotFoundException(){
    Long notExistingUserId = 5L;

    UpdateUserRequest request = UpdateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.updateUser(request, notExistingUserId));

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void UserService_DeleteUser_ShouldDeleteUser(){
    Long userIdForDelete = 1L;

    userService.deleteUser(userIdForDelete);

    verify(userRepository).deleteById(userIdForDelete);
  }

  @Test
  void UserService_GetUserById_ShouldReturnUser(){
    Long existingUserId = 1L;
    User existingUser = User.builder().id(existingUserId).build();

    when(userRepository.findById(existingUserId)).thenReturn(Optional.of(existingUser));

    User returnedUser = userService.getUserById(existingUserId);

    assertNotNull(returnedUser);
    assertEquals(returnedUser.getId(), existingUser.getId());
    verify(userRepository).findById(existingUserId);

  }

  @Test
  void UserService_GetUserById_ShouldThrowUserNotFoundException(){
    Long nonExistingUserId = 99L;

    when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.
            getUserById(nonExistingUserId));

    verify(userRepository).findById(nonExistingUserId);
  }

  @Test
  void UserService_getAllPaginatedAndFilteredUsers_ShouldReturnUsersList(){
    int pageNumber = 1;
    int pageSize = 1;
    String name = "Andy";
    String surname = "Jk";

    User firstUser = User.builder().name(name).surname(surname).build();
    User secondUser = User.builder().name(name).surname(surname).build();
    List<User> users = List.of(firstUser, secondUser);

    Page<User> pageOfUsers = new PageImpl<>(
        users,
        PageRequest.of(pageNumber, pageSize),
        users.size()
    );

    when(userRepository.findAll(
        any(Specification.class),
        eq(PageRequest.of(pageNumber, pageSize))
    )).thenReturn(pageOfUsers);

    List<User> result = userService
        .getAllPaginatedAndFilteredUsers(
            pageNumber,
            pageSize,
            name,
            surname
        );

    assertNotNull(result);
    assertThat(result.size()).isEqualTo(users.size());

    verify(userRepository).findAll(
        any(Specification.class), eq(PageRequest.of(pageNumber, pageSize)));
  }

  @Test
  void UserService_ActivateUser_ShouldActivateUser(){
    User user = User.builder().build();

    when(userRepository.save(user))
        .thenReturn(user);

    userService.activateUser(user);

    verify(userRepository).save(user);
  }

  @Test
  void UserService_DeactivateUser_ShouldActivateUser(){
    User user = User.builder().build();

    when(userRepository.save(user))
        .thenReturn(user);

    userService.deactivateUser(user);

    verify(userRepository).save(user);
  }
}
