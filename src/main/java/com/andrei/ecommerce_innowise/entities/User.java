package com.andrei.ecommerce_innowise.entities;

import com.andrei.ecommerce_innowise.audit.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String surname;

  @Column(nullable = false)
  private LocalDate birthDate;

  @Column(unique = true, nullable = false, columnDefinition = "TEXT")
  private String email;

  @Column(nullable = false)
  private boolean active;

  @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
  private List<PaymentCard> cards = new ArrayList<>();

  public User(String name, String surname, LocalDate birthDate, String email, boolean active) {
    this.name = name;
    this.surname = surname;
    this.birthDate = birthDate;
    this.email = email;
    this.active = active;
  }

  public User() {
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", surname='" + surname + '\'' +
        ", birthDate=" + birthDate +
        ", email='" + email + '\'' +
        ", active=" + active +
        ", cards=" + cards +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return active == user.active && Objects.equals(id, user.id) && Objects.equals(
        name, user.name) && Objects.equals(surname, user.surname)
        && Objects.equals(birthDate, user.birthDate) && Objects.equals(email,
        user.email) && Objects.equals(cards, user.cards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, surname, birthDate, email, active, cards);
  }
}
