package com.andrei.ecommerce_innowise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.exception.payment_card.PaymentCardNotFoundException;
import com.andrei.ecommerce_innowise.exception.payment_card.TooManyPaymentCardsException;
import com.andrei.ecommerce_innowise.exception.user.UserNotFoundException;
import com.andrei.ecommerce_innowise.mappers.PaymentCardMapper;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
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
public class PaymentCardServiceTest {

  @Mock
  private PaymentCardRepository cardRepository;

  @Mock
  private PaymentCardMapper mapper;

  @Mock
  private UserService userService;

  @InjectMocks
  private PaymentCardService cardService;

  @Test
  void PaymentCardService_CreateCard_ShouldCreateCardAndReturnCardResponse(){
    CreatePaymentCardRequest request =
        CreatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();

    PaymentCard card = PaymentCard.builder().build();
    PaymentCard savedCard = PaymentCard.builder().id(1L).build();
    PaymentCardResponse expectedResponse = PaymentCardResponse
        .builder()
        .id(1L)
        .build();
    User ownerOfTheCards = User.builder().build();

    when(mapper.paymentCardDtoToPaymentCard(request))
        .thenReturn(card);
    when(cardRepository.getCardsAmountByUserId(1L))
        .thenReturn(4L);
    when(userService.getUserById(1L))
        .thenReturn(ownerOfTheCards);
    when(mapper.paymentCardToPaymentCardResponse(savedCard))
        .thenReturn(expectedResponse);
    when(cardRepository.save(card)).thenReturn(savedCard);

    PaymentCardResponse result = cardService
        .createCard(request, 1L);

    assertNotNull(result);
    assertEquals(expectedResponse.getId(), result.getId());

    verify(mapper).paymentCardDtoToPaymentCard(request);
    verify(mapper).paymentCardToPaymentCardResponse(savedCard);
    verify(userService).getUserById(1L);
    verify(cardRepository).save(card);
    verify(cardRepository).getCardsAmountByUserId(1L);
  }

  @Test
  void PaymentCardService_CreateCard_ShouldThrowTooManyPaymentCardsException(){
    CreatePaymentCardRequest request =
        CreatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();
    when(cardRepository.getCardsAmountByUserId(1L))
        .thenReturn(5L);

    assertThrows(TooManyPaymentCardsException.class,
        () -> cardService.createCard(request, 1L));

    verify(cardRepository)
        .getCardsAmountByUserId(1L);
    verify(cardRepository, never())
        .save(any(PaymentCard.class));
  }

  @Test
  void PaymentCardService_UpdateCard_ShouldUpdateCardAndReturnResponse(){
    UpdatePaymentCardRequest request =
        UpdatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();

    PaymentCard existedCard = PaymentCard
        .builder()
        .id(1L)
        .build();

    PaymentCard updatedCard =
        PaymentCard.builder().id(1L).build();

    PaymentCardResponse expectedResponse =
        PaymentCardResponse.builder().id(1L).build();

    when(mapper
        .updatePaymentCardFromPaymentCardUpdateRequest(
            request, existedCard)).thenReturn(updatedCard);
    when(cardRepository.findById(1L))
        .thenReturn(Optional.of(existedCard));
    when(mapper.paymentCardToPaymentCardResponse(updatedCard))
        .thenReturn(expectedResponse);
    when(cardRepository.save(updatedCard))
        .thenReturn(updatedCard);

    PaymentCardResponse result =
        cardService.updateCard(request, 1L);

    assertNotNull(result);
    assertEquals(expectedResponse.getId(), result.getId());

    verify(mapper)
        .updatePaymentCardFromPaymentCardUpdateRequest(request, existedCard);
    verify(cardRepository).findById(1L);
    verify(mapper).paymentCardToPaymentCardResponse(updatedCard);
    verify(cardRepository).save(updatedCard);
  }

  @Test
  void PaymentCardService_UpdateCard_ShouldThrowPaymentCardNotFoundException(){
    UpdatePaymentCardRequest request =
        UpdatePaymentCardRequest
            .builder()
            .number("4111111111111111")
            .holder("John Rodriguez")
            .expirationDate((LocalDate.now()))
            .build();

    assertThrows
        (
        PaymentCardNotFoundException.class,
        () -> cardService.updateCard(request, 1L)
        );

    verify(cardRepository, never()).save(any(PaymentCard.class));
  }

  @Test
  void PaymentCardService_deleteCard_ShouldDeleteCard(){
    PaymentCard card =
        PaymentCard.builder().id(1L).build();

    when(cardRepository.findById(1L))
        .thenReturn(Optional.of(card));


    cardService.deleteCard(1L);

    verify(cardRepository).findById(1L);
    verify(cardRepository).delete(card);
  }

  @Test
  void PaymentCardService_getCardById_ShouldReturnCard(){
    PaymentCard card = PaymentCard.builder().id(1L).build();

    when(cardRepository.findById(1L))
        .thenReturn(Optional.of(card));

    PaymentCard result = cardService.getCardById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getId());

    verify(cardRepository).findById(1L);
  }

  @Test
  void PaymentCardService_getCardById_ShouldThrowPaymentCardNotFoundException(){

    assertThrows(
        PaymentCardNotFoundException.class,
        () -> cardService.getCardById(99L)
        );

    verify(cardRepository).findById(99L);

  }

  @Test
  void PaymentCardService_GetAllCardsByUserId_ShouldReturnCards(){
    PaymentCard card = PaymentCard.builder().build();
    List<PaymentCard> expectedCards = List.of(card);
    User user = User.builder().id(1L).build();
    card.setUser(user);

    when(userService.getUserById(1L)).thenReturn(user);
    when(cardRepository.getAllPaymentCardsByUserId(1L))
        .thenReturn(expectedCards);

    List<PaymentCard> result =
        cardService.getAllCardsByUserId(1L);

    assertNotNull(result);
    assertEquals(expectedCards.size(), result.size());

    verify(userService).getUserById(1L);
    verify(cardRepository).getAllPaymentCardsByUserId(1L);
  }

  @Test
  void PaymentCardService_GetAllCardsByUserId_ShouldThrowUserNotFoundException() {

    when(userService.getUserById(99L))
        .thenThrow(new UserNotFoundException("User not found"));

    assertThrows(
        UserNotFoundException.class,
        () -> cardService.getAllCardsByUserId(99L)
        );

    verify(userService).getUserById(99L);
  }

  @Test
  void PaymentCardService_ActivateCard_ShouldActivateCard(){
    PaymentCard cardForActivation =
        PaymentCard.builder().build();

    when(cardRepository.save(cardForActivation))
        .thenReturn(cardForActivation);

    cardService.activateCard(cardForActivation);

    verify(cardRepository).save(cardForActivation);

  }

  @Test
  void PaymentCardService_DeactivateCard_ShouldDeactivateCard(){
    PaymentCard cardForActivation =
        PaymentCard.builder().build();

    when(cardRepository.save(cardForActivation))
        .thenReturn(cardForActivation);

    cardService.deactivateCard(cardForActivation);

    verify(cardRepository).save(cardForActivation);

  }

  @Test
  void PaymentCardService_FetchAll_ShouldReturnFilteredAndPaginatedCardsByUser(){
    int pageNumber = 1;
    int pageSize = 1;
    String name = "John";
    String surname = "Smith";

    PaymentCard card1 = PaymentCard.builder().build();
    PaymentCard card2 = PaymentCard.builder().build();
    List<PaymentCard> cards = List.of(card1, card2);

    User cardsHolder = User.builder()
        .name(name).surname(surname)
        .cards(cards).build();

    card1.setUser(cardsHolder);
    card2.setUser(cardsHolder);

    Page<PaymentCard> cardsPage = new PageImpl<>(
        cards,
        PageRequest.of(pageNumber, pageSize),
        cards.size()
    );

    when(cardRepository
        .findAll(
            any(Specification.class),
            eq(PageRequest.of(pageNumber, pageSize))
        ))
        .thenReturn(cardsPage);

    List<PaymentCard> result =
        cardService.fetchAll(
            pageNumber,
            pageSize,
            name,
            surname
        );

    assertNotNull(result);
    assertEquals(cards.size(), result.size());
    assertEquals(cardsHolder.getName(), result.get(0).getUser().getName());
    assertEquals(cardsHolder.getSurname(), result.get(0).getUser().getSurname());
    assertEquals(cardsHolder.getName(), result.get(1).getUser().getName());
    assertEquals(cardsHolder.getSurname(), result.get(1).getUser().getSurname());

    verify(cardRepository).findAll
        (
            any(Specification.class),
            eq(PageRequest.of(pageNumber, pageSize))
            );
  }

}
