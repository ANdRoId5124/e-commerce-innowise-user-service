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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final static boolean ACTIVE = true;
  private final static boolean NOT_ACTIVE = false;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }


  @Caching(
      put = {@CachePut(value = "userResponse", key = "#result.id")},
      evict = {@CacheEvict(value = "usersPage", allEntries = true)}
  )

  public UserResponse createUser(CreateUserRequest createUserRequest){
    if(userRepository.findUserByEmail(createUserRequest.getEmail()).isPresent()){
      throw new IllegalArgumentException("Email is taken");
    }

    User userForCreation = userMapper.userDTOToUser(createUserRequest);
    userForCreation.setActive(ACTIVE);

    return userMapper
        .userToUserResponseDto(
            userRepository.save(userForCreation)
        );
  }

  @Transactional
  @Caching(
      put = {@CachePut(value = "userResponse", key = "#userId")},
      evict = {
          @CacheEvict(value = "userById", key = "#userId"),
          @CacheEvict(value = "usersPage", allEntries = true)
      }
  )
  public UserResponse updateUser(
      UpdateUserRequest userUpdateRequest,
      Long userId
  ){
    User userForUpdate =  getUserById(userId);

    User updatedUser = userRepository.save(
            userMapper
                .updateUserFromUserUpdateRequest(
                    userUpdateRequest,
                    userForUpdate
                )
        );

    return userMapper.userToUserResponseDto(updatedUser);
  }

  @Transactional
  @Caching(
      evict = {
          @CacheEvict(value = "userResponse", key = "#userId"),
          @CacheEvict(value = "usersPage", allEntries = true),
          @CacheEvict(value = "userById", key = "#userId"),
          @CacheEvict(value = "cardsByUserId", key = "#userId")
      }
  )
  public void deleteUser(Long userId){
    userRepository.deleteById(userId);
  }


  @Cacheable(value = "userById", key = "#id")
  public User getUserById(Long id){
    return userRepository
        .findById(id)
        .orElseThrow(() ->
            new UserNotFoundException("User not found: " + id));
  }

  @Cacheable(value = "usersPage",
      key = "{#pageNumber, #pageSize, #name, #surname}"
  )
  public List<User> getAllPaginatedAndFilteredUsers(
      int pageNumber,
      int pageSize,
      String name,
      String surname){
    return userRepository.findAll(UserSpecification.withFilters(name, surname),
        PageRequest.of(pageNumber, pageSize)).
        getContent();
  }

  @Caching(
      evict = {
          @CacheEvict(value = "userById", key = "#user.id"),
          @CacheEvict(value = "usersPage", allEntries = true),
          @CacheEvict(value = "userResponse", key = "#user.id")
      }
  )
  public void activateUser(User user){
    user.setActive(ACTIVE);
    userRepository.save(user);
  }

  @Caching(
      evict = {
          @CacheEvict(value = "userById", key = "#user.id"),
          @CacheEvict(value = "usersPage", allEntries = true),
          @CacheEvict(value = "userResponse", key = "#user.id")
      }
  )
  public void deactivateUser(User user){
    user.setActive(NOT_ACTIVE);
    userRepository.save(user);
  }
}
