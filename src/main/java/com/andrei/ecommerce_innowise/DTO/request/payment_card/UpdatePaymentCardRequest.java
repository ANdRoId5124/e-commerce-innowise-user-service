package com.andrei.ecommerce_innowise.DTO.request.payment_card;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.hibernate.validator.constraints.CreditCardNumber;

public class UpdatePaymentCardRequest {

  @CreditCardNumber(message = "Payment card number is invalid")
  private String number;

  @Size(min = 2, message = "Holder cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Holder cannot start or end with whitespace"
  )
  private String holder;

  private LocalDate expirationDate;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getHolder() {
    return holder;
  }

  public void setHolder(String holder) {
    this.holder = holder;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }
}
