package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.exception.payment_card.PaymentCardNotFoundException;
import com.andrei.ecommerce_innowise.exception.user.UserNotFoundException;
import com.andrei.ecommerce_innowise.mappers.PaymentCardMapper;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import com.andrei.ecommerce_innowise.specification.PaymentCardSpecification;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentCardService {
  private final PaymentCardRepository cardRepository;
  private final UserRepository userRepository;
  private final static int MAX_CARDS_AMOUNT = 5;
  private final static boolean ACTIVE = true;
  private final static boolean NOT_ACTIVE = false;

  public PaymentCardService(PaymentCardRepository cardRepository, UserRepository userRepository) {
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
  }

  public PaymentCardResponse createCard(CreatePaymentCardRequest cardDto, Long userId){
    if( cardRepository.getCardsAmountByUserId(userId) >= MAX_CARDS_AMOUNT){
      throw new RuntimeException("Too many cards");
    }
    PaymentCard createdPaymentCard = PaymentCardMapper
        .Instance
        .paymentCardDtoToPaymentCard(cardDto);

    createdPaymentCard.setActive(ACTIVE);

    return PaymentCardMapper
        .Instance
        .paymentCardToPaymentCardResponse(createdPaymentCard);
  }

  @Transactional
  public PaymentCardResponse updateCard(
      UpdatePaymentCardRequest updatePaymentCardRequest,
      Long cardId
  ){
    PaymentCard updatedCard = cardRepository.save(
        PaymentCardMapper
            .Instance
            .updatePaymentCardFromPaymentCardUpdateRequest(
                updatePaymentCardRequest,
                getCardById(cardId)
            )
    );
    return PaymentCardMapper
        .Instance
        .paymentCardToPaymentCardResponse(updatedCard);
  }

  @Transactional
  public void deleteCard(Long cardId){
    cardRepository.deleteById(cardId);
  }

  public PaymentCard getCardById(Long id){
    return cardRepository.findById(id)
        .orElseThrow(() ->
            new PaymentCardNotFoundException("Card not found: " + id));
  }

  public List<PaymentCard> getAllCardsByUserId(Long userId){
    if(!userRepository.existsById(userId)){
      throw new UserNotFoundException("User not found: " + userId);
    }
    return cardRepository.getAllPaymentCardsByUserId(userId);
  }

  public List<PaymentCard> fetchAll(
      int pageNumber,
      int pageSize,
      String userName,
      String userSurname){
    return cardRepository.findAll(
        PaymentCardSpecification.withFilters(userName, userSurname),
        PageRequest.of(pageNumber, pageSize)).getContent();
  }

  public void activateCard(PaymentCard paymentCard){
    paymentCard.setActive(ACTIVE);
    cardRepository.save(paymentCard);
  }

  public void deactivateCard(PaymentCard paymentCard){
    paymentCard.setActive(NOT_ACTIVE);
    cardRepository.save(paymentCard);
  }

}
