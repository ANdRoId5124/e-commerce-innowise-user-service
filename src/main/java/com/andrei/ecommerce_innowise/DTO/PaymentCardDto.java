package com.andrei.ecommerce_innowise.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.hibernate.validator.constraints.CreditCardNumber;

public class PaymentCardDto {

  @NotBlank(message = "Payment card number is required")
  @CreditCardNumber(message = "Payment card number is invalid")
  private String number;

  @NotBlank(message = "Holder is required")
  @Size(min = 2, message = "Holder cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Holder cannot start or end with whitespace"
  )
  private String holder;

  @NotNull(message = "The expiration date is required")
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
