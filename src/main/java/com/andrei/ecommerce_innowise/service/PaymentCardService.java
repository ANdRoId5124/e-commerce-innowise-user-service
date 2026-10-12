package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
import com.andrei.ecommerce_innowise.specification.PaymentCardSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentCardService {
  private final PaymentCardRepository cardRepository;
  final static int MAX_CARDS_AMOUNT = 5;

  public PaymentCardService(PaymentCardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public PaymentCard createCard(PaymentCard card, Long userId){
    if( cardRepository.getCardsAmountByUserId(userId) >= MAX_CARDS_AMOUNT){
      throw new RuntimeException("Too many cards");
    }
    return cardRepository.save(card);
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
