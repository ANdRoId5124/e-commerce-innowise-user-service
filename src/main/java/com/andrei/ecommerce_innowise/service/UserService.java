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
