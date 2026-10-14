package com.andrei.ecommerce_innowise.exception;

import com.andrei.ecommerce_innowise.exception.payment_card.PaymentCardNotFoundException;
import com.andrei.ecommerce_innowise.exception.payment_card.TooManyPaymentCardsException;
import com.andrei.ecommerce_innowise.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(
      {
          UserNotFoundException.class,
          PaymentCardNotFoundException.class
      }
  )
  public ResponseEntity<String> handleExceptions(Exception ex){
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ExceptionHandler(
      value = TooManyPaymentCardsException.class
  )
  public ResponseEntity<String> handleTooManyCardsException(
      TooManyPaymentCardsException ex
  ){
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage()
        );
  }

}
