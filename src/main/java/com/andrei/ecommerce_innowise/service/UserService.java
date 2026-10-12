package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import com.andrei.ecommerce_innowise.specification.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user){
    return userRepository.save(user);
  }

  public User updateUser(User user){
    return userRepository.save(user);
  }

  public User getUserById(Long id){
    return userRepository
        .findById(id)
        .orElseThrow(() ->
            new EntityNotFoundException("User not found: " + id));
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
}
