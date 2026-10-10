package com.andrei.ecommerce_innowise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ECommerceInnowiseApplication {

  public static void main(String[] args) {
    SpringApplication.run(ECommerceInnowiseApplication.class, args);
  }

}
