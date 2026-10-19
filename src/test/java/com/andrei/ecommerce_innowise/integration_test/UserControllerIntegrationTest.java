package com.andrei.ecommerce_innowise.integration_test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andrei.ecommerce_innowise.DTO.request.user.CreateUserRequest;
import com.andrei.ecommerce_innowise.DTO.request.user.UpdateUserRequest;
import com.andrei.ecommerce_innowise.DTO.response.user.UserResponse;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;


@Testcontainers
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = "spring.cache.type=none"
)
@AutoConfigureTestRestTemplate
public class UserControllerIntegrationTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer postgres =
      new PostgreSQLContainer("postgres:17");

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  UserRepository userRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void shouldReturnPostgresContainer(){
    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldCreateUserAndReturnCreatedStatus(){
    CreateUserRequest request = CreateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    ResponseEntity<UserResponse> response =
        restTemplate.postForEntity(
            "api/users/creation",
            request,
            UserResponse.class
        );

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());

    assertTrue(userRepository.findUserByEmail(request.getEmail()).isPresent());
  }

  @Test
  void shouldThrowIllegalArgumentException(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    userRepository.save(user);

    CreateUserRequest request = CreateUserRequest
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    ResponseEntity<String> response =
        restTemplate.postForEntity(
            "api/users/creation",
            request,
            String.class
        );

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void shouldUpdateUserAndReturnOkStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);
    User testUser = userRepository.findById(savedUser.getId()).get();

    UpdateUserRequest request = UpdateUserRequest
        .builder()
        .name("Alan")
        .surname("Mac")
        .birthDate(LocalDate.of(1990,10,12))
        .email("exampleForUpdate@email.com")
        .build();

    ResponseEntity<UserResponse> response =
        restTemplate
            .exchange(
                "api/users/" + testUser.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UserResponse.class
            );


    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(userRepository.findUserByEmail(request.getEmail()).isPresent());
    assertEquals(request.getName(),
        userRepository.findUserByEmail(request.getEmail()).get().getName());
    assertEquals(request.getSurname(),
        userRepository.findUserByEmail(request.getEmail()).get().getSurname());
  }

  @Test
  void shouldThrowNotFoundException(){
    Long nonExistedUserId = 1L;

    UpdateUserRequest request = UpdateUserRequest
        .builder()
        .name("Alan")
        .surname("Mac")
        .birthDate(LocalDate.of(1990,10,12))
        .email("exampleForUpdate@email.com")
        .build();

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/users/" + nonExistedUserId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                String.class
            );


    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldDeleteUserAndReturnOkStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);
    assertTrue(userRepository.findUserByEmail(user.getEmail()).isPresent());

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/users/" + savedUser.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(userRepository.findUserByEmail(user.getEmail()).isPresent());

  }

  @Test
  void shouldActivateUserAndReturnOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .active(false)
        .build();

    User savedUser = userRepository.save(user);

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/users/activation/" + savedUser.getId(),
                HttpMethod.PATCH,
                null,
                Void.class
                );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(userRepository.findUserByEmail(user.getEmail()).get().isActive());

  }

  @Test
  void shouldThrowNotFoundExceptionForUserActivation(){
    Long nonExistedUserId = 1L;

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/users/activation/" + nonExistedUserId,
                HttpMethod.PATCH,
                null,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }


  @Test
  void shouldDeactivateUserAndReturnOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .active(true)
        .build();

    User savedUser = userRepository.save(user);

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/users/deactivation/" + savedUser.getId(),
                HttpMethod.PATCH,
                null,
                Void.class
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(userRepository.findUserByEmail(user.getEmail()).get().isActive());
  }

  @Test
  void shouldThrowNotFoundExceptionForUserDeactivation(){
    Long nonExistedUserId = 1L;

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/users/deactivation/" + nonExistedUserId,
                HttpMethod.PATCH,
                null,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldReturnUserByIdAndOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .active(true)
        .build();

    User savedUser = userRepository.save(user);

    ResponseEntity<User> response =
        restTemplate
            .getForEntity(
                "api/users/" + savedUser.getId(),
                User.class
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(savedUser.getId(), response.getBody().getId());
  }


  @Test
  void shouldGetAllFilteredUsersAndReturnOkStatus() {

    User user1 = User.builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990, 10, 12))
        .email("example@email.com")
        .active(true)
        .build();

    User user2 = User.builder()
        .name("John")
        .surname("Doe")
        .birthDate(LocalDate.of(1985, 5, 5))
        .email("example1@email.com")
        .active(true)
        .build();

    userRepository.saveAll(List.of(user1, user2));

    String url = UriComponentsBuilder.fromPath("/api/users/all")
        .queryParam("page-number", 1)
        .queryParam("page-size", 10)
        .queryParam("name", "Alex")
        .toUriString();


    ResponseEntity<List<User>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<User>>() {}
    );


    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Alex", response.getBody().get(0).getName());
  }

}
