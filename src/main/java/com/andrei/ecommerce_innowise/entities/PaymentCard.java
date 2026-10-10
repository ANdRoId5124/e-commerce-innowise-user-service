package com.andrei.ecommerce_innowise.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "payment_cards")
public class PaymentCard {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_payment_cards_user")
  )
  private User user;

}
