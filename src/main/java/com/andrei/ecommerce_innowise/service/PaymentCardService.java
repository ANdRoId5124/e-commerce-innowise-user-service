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

  public PaymentCardService(PaymentCardRepository cardRepository) {
    this.cardRepository = cardRepository;
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
