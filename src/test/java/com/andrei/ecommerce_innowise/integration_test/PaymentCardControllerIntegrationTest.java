package com.andrei.ecommerce_innowise.integration_test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
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
public class PaymentCardControllerIntegrationTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer postgres =
      new PostgreSQLContainer("postgres:17");

  @Autowired
  PaymentCardRepository cardRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestRestTemplate restTemplate;

  @AfterEach
  void tearDown(){
    userRepository.deleteAll();
    cardRepository.deleteAll();
  }

  @Test
  void shouldReturnPostgresContainer(){
    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldReturnCardAndCreatedStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    CreatePaymentCardRequest request =
        CreatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();

    ResponseEntity<PaymentCardResponse> response =
        restTemplate
            .postForEntity(
                "api/payment-cards/" + savedUser.getId(),
                request,
                PaymentCardResponse.class
            );

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(cardRepository.findById(response.getBody().getId()).isPresent());

  }

  @Test
  void shouldThrowNotFoundExceptionForCardCreation(){
    Long nonExistedUserId = 1L;

    CreatePaymentCardRequest request =
        CreatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();

    ResponseEntity<String> response =
        restTemplate
            .postForEntity(
                "api/payment-cards/" + nonExistedUserId,
                request,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldDeleteCardAndReturnOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .user(savedUser)
            .build();

    PaymentCard savedCard = cardRepository.save(card);

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/payment-cards/" + savedCard.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(cardRepository.findById(savedCard.getId()).isPresent());
  }

  @Test
  void shouldNotFoundExceptionForDeleteCardAttempt(){
    Long nonExistedPaymentCardId = 1L;

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/payment-cards/" + nonExistedPaymentCardId,
                HttpMethod.DELETE,
                null,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldReturnCardByIdAndOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .user(savedUser)
            .build();

    PaymentCard savedCard = cardRepository.save(card);

    ResponseEntity<PaymentCard> response =
        restTemplate
            .getForEntity(
                "api/payment-cards/" + savedCard.getId(),
                PaymentCard.class
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(card.getId(), response.getBody().getId());
  }

  @Test
  void shouldThrowPaymentCardNotFoundExceptionForGetCardByIdAttempt(){
    Long nonExistedCardId = 1L;

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/payment-cards/" + nonExistedCardId,
                HttpMethod.GET,
                null,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldUpdateCardAndReturnOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .user(savedUser)
            .build();

    PaymentCard savedCard = cardRepository.save(card);

    UpdatePaymentCardRequest request =
        UpdatePaymentCardRequest
            .builder()
            .number("5555555555554444")
            .holder("Emily Johnson")
            .expirationDate(LocalDate.now().plusYears(3))
            .build();

    ResponseEntity<PaymentCardResponse> response =
        restTemplate
            .exchange(
                "api/payment-cards/" + savedCard.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                PaymentCardResponse.class
            );

    PaymentCard updatedCard = cardRepository.findById(savedCard.getId()).get();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedCard.getId(), response.getBody().getId());
    assertEquals(request.getNumber(), updatedCard.getNumber());
    assertEquals(request.getHolder(), updatedCard.getHolder());
    assertEquals(request.getExpirationDate(), updatedCard.getExpirationDate());

  }

  @Test
  void shouldThrowPaymentCardNotFoundExceptionAfterCardUpdateAttempt(){
    Long nonExistedCardId = 1L;

    UpdatePaymentCardRequest request =
        UpdatePaymentCardRequest
            .builder()
            .number("5555555555554444")
            .holder("Emily Johnson")
            .expirationDate(LocalDate.now().plusYears(3))
            .build();

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/payment-cards/" + nonExistedCardId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldActivateCardAndReturnOkStatusResponse(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .active(false)
            .user(savedUser)
            .build();

    PaymentCard savedCard = cardRepository.save(card);

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/payment-cards/activation/" + savedCard.getId(),
                HttpMethod.PATCH,
                null,
                Void.class
            );

    PaymentCard activatedCard = cardRepository.findById(savedCard.getId()).get();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(savedCard.getId(), activatedCard.getId());
    assertTrue(activatedCard.isActive());
  }

  @Test
  void shouldThrowPaymentCardNotFoundExceptionAfterCardActivationAttempt(){
    Long nonExistedCardId = 1L;

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/payment-cards/activation/" + nonExistedCardId,
                HttpMethod.PATCH,
                null,
                Void.class
            );


    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldDeactivateCardAndReturnOkStatusResponse(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .active(true)
            .user(savedUser)
            .build();

    PaymentCard savedCard = cardRepository.save(card);

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/payment-cards/deactivation/" + savedCard.getId(),
                HttpMethod.PATCH,
                null,
                Void.class
            );

    PaymentCard activatedCard = cardRepository.findById(savedCard.getId()).get();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(savedCard.getId(), activatedCard.getId());
    assertFalse(activatedCard.isActive());
  }

  @Test
  void shouldThrowPaymentCardNotFoundExceptionAfterCardDeactivationAttempt(){
    Long nonExistedCardId = 1L;

    ResponseEntity<Void> response =
        restTemplate
            .exchange(
                "api/payment-cards/deactivation/" + nonExistedCardId,
                HttpMethod.PATCH,
                null,
                Void.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldReturnCardsByUserIdAndOkResponseStatus(){
    User user =  User
        .builder()
        .name("Alex")
        .surname("MacDougle")
        .birthDate(LocalDate.of(1990,10,12))
        .email("example@email.com")
        .build();

    User savedUser = userRepository.save(user);

    PaymentCard card1 =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .active(true)
            .user(savedUser)
            .build();

    PaymentCard card2 =
        PaymentCard
            .builder()
            .number("5555555555554444")
            .holder("Emily Johnson")
            .expirationDate(LocalDate.now().plusYears(3))
            .active(true)
            .user(savedUser)
            .build();

    List<PaymentCard> userCards = cardRepository.saveAll(List.of(card1, card2));



    ResponseEntity<List<PaymentCard>> response =
        restTemplate
            .exchange(
                "api/payment-cards/users/" + savedUser.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentCard>>() {}
            );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userCards.size(), response.getBody().size());
    assertEquals(userCards.get(0).getId(), response.getBody().get(0).getId());
    assertEquals(userCards.get(1).getId(), response.getBody().get(1).getId());
  }


  @Test
  void shouldThrowUserNotFoundExceptionAfterGetAllCardByUserIdAttempt(){
    Long nonExistedUserId = 1L;

    ResponseEntity<String> response =
        restTemplate
            .exchange(
                "api/payment-cards/users/" + nonExistedUserId,
                HttpMethod.GET,
                null,
                String.class
            );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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

    List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));

    PaymentCard card1 =
        PaymentCard
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .active(true)
            .user(savedUsers.get(0))
            .build();

    PaymentCard card2 =
        PaymentCard
            .builder()
            .number("5555555555554444")
            .holder("Emily Johnson")
            .expirationDate(LocalDate.now().plusYears(3))
            .active(true)
            .user(savedUsers.get(1))
            .build();

    cardRepository.saveAll(List.of(card1, card2));

    List<PaymentCard> savedCards =
        cardRepository.saveAll(List.of(card1, card2));



    String url = UriComponentsBuilder.fromPath("/api/payment-cards/all")
        .queryParam("page-number", 1)
        .queryParam("page-size", 10)
        .queryParam("name", "Alex")
        .toUriString();


    ResponseEntity<List<PaymentCard>> response =
        restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<PaymentCard>>() {}
    );


    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(savedCards.get(0).getId(), response.getBody().get(0).getId());
  }


}
