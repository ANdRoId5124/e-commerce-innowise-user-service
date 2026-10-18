package com.andrei.ecommerce_innowise.service;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.entities.User;
import com.andrei.ecommerce_innowise.exception.payment_card.PaymentCardNotFoundException;
import com.andrei.ecommerce_innowise.exception.payment_card.TooManyPaymentCardsException;
import com.andrei.ecommerce_innowise.mappers.PaymentCardMapper;
import com.andrei.ecommerce_innowise.repository.PaymentCardRepository;
import com.andrei.ecommerce_innowise.specification.PaymentCardSpecification;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentCardService {
  private final PaymentCardRepository cardRepository;
  private final UserService userService;
  private final PaymentCardMapper paymentCardMapper;
  private final static int MAX_CARDS_AMOUNT = 5;
  private final static boolean ACTIVE = true;
  private final static boolean NOT_ACTIVE = false;

  public PaymentCardService(PaymentCardRepository cardRepository, UserService userService,
      PaymentCardMapper paymentCardMapper) {
    this.cardRepository = cardRepository;
    this.userService = userService;
    this.paymentCardMapper = paymentCardMapper;
  }

  @Caching(
      put = {@CachePut(value = "cardResponse", key = "#result.id")},
      evict = {
          @CacheEvict(value = "cardsByUserId", allEntries = true),
          @CacheEvict(value = "cardsPage", allEntries = true)
      }
  )
  public PaymentCardResponse createCard(CreatePaymentCardRequest cardRequest, Long userId){
    if( cardRepository.getCardsAmountByUserId(userId) >= MAX_CARDS_AMOUNT){
      throw new TooManyPaymentCardsException("Too many cards");
    }
    PaymentCard createdPaymentCard = paymentCardMapper
        .paymentCardDtoToPaymentCard(cardRequest);

    createdPaymentCard.setUser(userService.getUserById(userId));
    createdPaymentCard.setActive(ACTIVE);

    return paymentCardMapper
        .paymentCardToPaymentCardResponse(
            cardRepository.
                save(createdPaymentCard)
        );
  }

  @Transactional
  @Caching(
      put = {@CachePut(value = "cardResponse", key = "#result.id")},
      evict = {
          @CacheEvict(value = "cardsByUserId", allEntries = true),
          @CacheEvict(value = "cardsPage", allEntries = true),
          @CacheEvict(value = "cardById", key = "#cardId")
      }
  )
  public PaymentCardResponse updateCard(
      UpdatePaymentCardRequest updatePaymentCardRequest,
      Long cardId
  ){

    PaymentCard updatedCard = cardRepository.save(
        paymentCardMapper
            .updatePaymentCardFromPaymentCardUpdateRequest(
                updatePaymentCardRequest,
                getCardById(cardId)
            )
    );
    return paymentCardMapper
        .paymentCardToPaymentCardResponse(updatedCard);
  }

  @Transactional
  @Caching(
      evict = {
          @CacheEvict(value = "cardById", key = "#cardId"),
          @CacheEvict(value = "cardResponse", key = "#cardId"),
          @CacheEvict(value = "cardsByUserId", allEntries = true),
          @CacheEvict(value = "cardsPage", allEntries = true)
      }
  )
  public void deleteCard(Long cardId){
    cardRepository.deleteById(cardId);
  }

  @Cacheable(value = "cardById", key = "#id")
  public PaymentCard getCardById(Long id){
    return cardRepository.findById(id)
        .orElseThrow(() ->
            new PaymentCardNotFoundException("Card not found: " + id));
  }

  @Cacheable(value = "cardsByUserId", key = "#userId")
  public List<PaymentCard> getAllCardsByUserId(Long userId){
    User user = userService.getUserById(userId);
    return cardRepository.getAllPaymentCardsByUserId(user.getId());
  }

  @Cacheable(
      value = "cardsPage",
      key = "{#pageNumber, #pageSize, #userName, #userSurname}"
  )
  public List<PaymentCard> fetchAll(
      int pageNumber,
      int pageSize,
      String userName,
      String userSurname){
    return cardRepository.findAll(
        PaymentCardSpecification.withFilters(userName, userSurname),
        PageRequest.of(pageNumber, pageSize)).getContent();
  }

  @Caching(
      evict = {
          @CacheEvict(value = "cardsPage", allEntries = true),
          @CacheEvict(value = "cardById", key = "#paymentCard.id"),
          @CacheEvict(value = "cardsByUserId", allEntries = true),
          @CacheEvict(value = "cardResponse", key = "#paymentCard.id")
      }
  )
  public void activateCard(PaymentCard paymentCard){
    paymentCard.setActive(ACTIVE);
    cardRepository.save(paymentCard);
  }

  @Caching(
      evict = {
          @CacheEvict(value = "cardsPage", allEntries = true),
          @CacheEvict(value = "cardById", key = "#paymentCard.id"),
          @CacheEvict(value = "cardsByUserId", allEntries = true),
          @CacheEvict(value = "cardResponse", key = "#paymentCard.id")
      }
  )
  public void deactivateCard(PaymentCard paymentCard){
    paymentCard.setActive(NOT_ACTIVE);
    cardRepository.save(paymentCard);
  }

}
