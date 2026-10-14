package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.DTO.request.user.CreateUserRequest;
import com.andrei.ecommerce_innowise.DTO.request.user.UpdateUserRequest;
import com.andrei.ecommerce_innowise.DTO.response.user.UserResponse;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.exception.user.UserNotFoundException;
import com.andrei.ecommerce_innowise.mappers.UserMapper;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import com.andrei.ecommerce_innowise.specification.UserSpecification;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final static boolean ACTIVE = true;
  private final static boolean NOT_ACTIVE = false;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserResponse createUser(CreateUserRequest createUserRequest){
    User userForCreation = UserMapper
        .Instance
        .userDTOToUser(createUserRequest);
    userForCreation.setActive(ACTIVE);

    return UserMapper
        .Instance
        .userToUserResponseDto(
            userRepository.save(userForCreation)
        );
  }

  @Transactional
  public UserResponse updateUser(
      UpdateUserRequest userUpdateRequest,
      Long userId
  ){
    User userForUpdate =  getUserById(userId);

    User updatedUser = userRepository.save(
            UserMapper
                .Instance
                .updateUserFromUserUpdateRequest(
                    userUpdateRequest,
                    userForUpdate
                )
        );

    return UserMapper
        .Instance
        .userToUserResponseDto(updatedUser);
  }

  @Transactional
  public void deleteUser(Long userId){
    userRepository.deleteById(userId);
  }

  public User getUserById(Long id){
    return userRepository
        .findById(id)
        .orElseThrow(() ->
            new UserNotFoundException("User not found: " + id));
  }

  public List<User> getAllPaginatedAndFilteredUsers(
      int pageNumber,
      int pageSize,
      String name,
      String surname){
    return userRepository.findAll(UserSpecification.withFilters(name, surname),
        PageRequest.of(pageNumber, pageSize)).
        getContent();
  }

  public void activateUser(User user){
    user.setActive(ACTIVE);
    userRepository.save(user);
  }

  public void deactivateUser(User user){
    user.setActive(NOT_ACTIVE);
    userRepository.save(user);
  }
}
