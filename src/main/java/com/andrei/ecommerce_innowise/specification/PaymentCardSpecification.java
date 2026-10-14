package com.andrei.ecommerce_innowise.specification;

import static com.andrei.ecommerce_innowise.specification.UserSpecification.USER_FIELD_NAME_FOR_FIRST_NAME;
import static com.andrei.ecommerce_innowise.specification.UserSpecification.USER_FIELD_NAME_FOR_SURNAME;

import com.andrei.ecommerce_innowise.entities.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {
  final static String NAME_OF_THE_USER_FILED_IN_THE_PAYMENT_CARD = "user";

  public static Specification<PaymentCard> hasCardUserName(String userName){
    if(userName == null || userName.isBlank()){
      return Specification.unrestricted();
    }

    return (
        root,
        query,
        criteriaBuilder) -> criteriaBuilder.equal(
            root.get(NAME_OF_THE_USER_FILED_IN_THE_PAYMENT_CARD).
                get(USER_FIELD_NAME_FOR_FIRST_NAME), userName);
  }

  public static Specification<PaymentCard> hasCardUserSurname(String userSurname){
    if(userSurname == null || userSurname.isBlank()){
      return Specification.unrestricted();
    }

    return (
        root,
        query,
        criteriaBuilder) -> criteriaBuilder.equal(
            root.get(NAME_OF_THE_USER_FILED_IN_THE_PAYMENT_CARD).
                get(USER_FIELD_NAME_FOR_SURNAME), userSurname);
  }

  public static Specification<PaymentCard> withFilters(String userName, String surname){
    return Specification.allOf(
        hasCardUserName(userName),
        hasCardUserSurname(surname));
  }

}
