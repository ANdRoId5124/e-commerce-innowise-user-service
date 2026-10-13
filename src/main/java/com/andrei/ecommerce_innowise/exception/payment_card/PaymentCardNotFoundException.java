package com.andrei.ecommerce_innowise.exception.payment_card;

public class PaymentCardNotFoundException extends RuntimeException {

  public PaymentCardNotFoundException(String message) {
    super(message);
  }
}
