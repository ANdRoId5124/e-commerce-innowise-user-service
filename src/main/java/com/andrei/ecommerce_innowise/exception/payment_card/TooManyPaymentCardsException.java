package com.andrei.ecommerce_innowise.exception.payment_card;

public class TooManyPaymentCardsException extends RuntimeException {

  public TooManyPaymentCardsException(String message) {
    super(message);
  }
}
