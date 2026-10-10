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

}
