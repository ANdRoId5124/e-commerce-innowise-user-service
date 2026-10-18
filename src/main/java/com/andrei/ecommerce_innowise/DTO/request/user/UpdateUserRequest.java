package com.andrei.ecommerce_innowise.DTO.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

  @Size(min = 2, message = "Name cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Name cannot start or end with whitespace"
  )
  private String name;

  @Size(min = 2, message = "Surname cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Surname cannot start or end with whitespace"
  )
  private String surname;

  @Past(message = "Birthday cannot be today or in the future")
  private LocalDate birthDate;

  @Email(message = "Invalid email")
  @Size(max = 200, message = "Max value of the email characters is 200")
  private String email;
}
