package com.andrei.ecommerce_innowise.DTO.request.payment_card;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
