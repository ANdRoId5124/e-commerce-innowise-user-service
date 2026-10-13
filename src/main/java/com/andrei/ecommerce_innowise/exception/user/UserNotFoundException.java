package com.andrei.ecommerce_innowise.exception.user;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
