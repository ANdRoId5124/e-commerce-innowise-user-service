package com.andrei.ecommerce_innowise.entities;

import com.andrei.ecommerce_innowise.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "payment_cards")

public class PaymentCard extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(
      unique = true,
      nullable = false,
      columnDefinition = "TEXT"
  )
  private String number;

  @Column(
      nullable = false, columnDefinition = "TEXT")
  private String holder;

  @Column(nullable = false)
  private LocalDate expirationDate;

  @Column(nullable = false)
  private boolean active;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_payment_cards_user")
  )
  private User user;

  public PaymentCard(String number, String holder, LocalDate expirationDate, boolean active,
      User user) {
    this.number = number;
    this.holder = holder;
    this.expirationDate = expirationDate;
    this.active = active;
    this.user = user;
  }

  public PaymentCard() {
  }

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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "PaymentCard{" +
        "id=" + id +
        ", number='" + number + '\'' +
        ", holder='" + holder + '\'' +
        ", expirationDate=" + expirationDate +
        ", active=" + active +
        ", user=" + user +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentCard that = (PaymentCard) o;
    return active == that.active && Objects.equals(id, that.id) && Objects.equals(
        number, that.number) && Objects.equals(holder, that.holder)
        && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(
        user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, number, holder, expirationDate, active, user);
  }
}
