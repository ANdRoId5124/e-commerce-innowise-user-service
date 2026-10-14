package com.andrei.ecommerce_innowise.contoroller;

import com.andrei.ecommerce_innowise.DTO.request.user.CreateUserRequest;
import com.andrei.ecommerce_innowise.DTO.request.user.UpdateUserRequest;
import com.andrei.ecommerce_innowise.DTO.response.user.UserResponse;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
  private final UserService userService;
  private static final int PAGE_INDEX_OFFSET = 1;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("creation")
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody CreateUserRequest createUserRequest
  ){
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
            userService
                .createUser(createUserRequest)
        );
  }

  @PutMapping("{id}")
  public ResponseEntity<UserResponse> updateUser(
      @Valid @RequestBody UpdateUserRequest updateUserRequest,
      @PathVariable Long id
  ){
    return ResponseEntity.ok(
        userService
            .updateUser(updateUserRequest, id)
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteUser(
      @PathVariable Long id
  ){
    userService.deleteUser(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("{id}")
  public ResponseEntity<User> getUserById(
      @PathVariable Long id
  ){
    return ResponseEntity.ok(
        userService
            .getUserById(id)
    );
  }

  @GetMapping("all")
  public ResponseEntity<List<User>> getAllFilteredUsers(
      @RequestParam(name = "page-number", required = false, defaultValue = "1") int pageNumber,
      @RequestParam(name = "page-size", required = false, defaultValue = "10") int pageSize,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "surname", required = false) String surname
  ){
    return ResponseEntity.ok(
        userService
            .getAllPaginatedAndFilteredUsers(
                pageNumber - PAGE_INDEX_OFFSET,
                pageSize,
                name,
                surname
            )
    );
  }

  @PatchMapping("activation/{id}")
  public ResponseEntity<Void> activateUser(
      @PathVariable Long id
  ){
    User userForActivation = userService.getUserById(id);
    userService.activateUser(userForActivation);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("deactivation/{id}")
  public ResponseEntity<Void> deactivateUser(
      @PathVariable Long id
  ){
    User userForDeactivation = userService.getUserById(id);
    userService.deactivateUser(userForDeactivation);
    return ResponseEntity.ok().build();
  }
}
