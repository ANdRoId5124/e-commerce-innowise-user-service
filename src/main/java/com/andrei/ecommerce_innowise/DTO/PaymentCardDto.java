package com.andrei.ecommerce_innowise.DTO;

import java.time.LocalDate;

public class PaymentCardDto {

  private String number;

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
