package com.andrei.ecommerce_innowise.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UserDto {

  @NotBlank(message = "Name is required")
  @Size(min = 2, message = "Name cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Name cannot start or end with whitespace"
  )
  private String name;

  @NotBlank(message = "Surname is required")
  @Size(min = 2, message = "Surname cannot be less than two characters")
  @Pattern(
      regexp = "^\\S(?:.*\\S)?$",
      message = "Surname cannot start or end with whitespace"
  )
  private String surname;

  @NotNull(message = "The birthdate is required")
  @Past(message = "Birthday cannot be today or in the future")
  private LocalDate birthDate;

  @NotBlank(message = "User email is required")
  @Email(message = "Invalid email")
  @Size(max = 200, message = "Max value of the email characters is 200")
  private String email;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
