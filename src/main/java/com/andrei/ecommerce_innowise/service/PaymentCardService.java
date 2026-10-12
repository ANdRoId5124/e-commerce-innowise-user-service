package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
import com.andrei.ecommerce_innowise.repository.UserRepository;
import com.andrei.ecommerce_innowise.specification.PaymentCardSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentCardService {
  private final PaymentCardRepository cardRepository;
  private final UserRepository userRepository;
  final static int MAX_CARDS_AMOUNT = 5;

  public PaymentCardService(PaymentCardRepository cardRepository, UserRepository userRepository) {
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
  }

  public PaymentCard createCard(PaymentCard card, Long userId){
    if( cardRepository.getCardsAmountByUserId(userId) >= MAX_CARDS_AMOUNT){
      throw new RuntimeException("Too many cards");
    }
    return cardRepository.save(card);
  }

  public PaymentCard updateCard(PaymentCard card){
    return cardRepository.save(card);
  }

  public PaymentCard getCardById(Long id){
    return cardRepository.findById(id)
        .orElseThrow(() ->
            new EntityNotFoundException("Card not found: " + id));
  }

  public List<PaymentCard> getAllCardsByUserId(Long userId){
    if(!userRepository.existsById(userId)){
      throw new EntityNotFoundException("Not found User by this id: " + userId);
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

}
