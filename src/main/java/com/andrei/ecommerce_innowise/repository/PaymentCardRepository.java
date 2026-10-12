package com.andrei.ecommerce_innowise.repository;

import com.andrei.ecommerce_innowise.entities.PaymentCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>,
    JpaSpecificationExecutor<PaymentCard> {

  long getCardsAmountByUserId(@Param("userId") Long userId);

}
