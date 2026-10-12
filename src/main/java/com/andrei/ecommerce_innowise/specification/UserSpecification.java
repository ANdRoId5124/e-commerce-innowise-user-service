package com.andrei.ecommerce_innowise.specification;

import com.andrei.ecommerce_innowise.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
  final static String USER_FIELD_NAME_FOR_FIRST_NAME = "name";
  final static String USER_FIELD_NAME_FOR_SURNAME = "surname";

  public static Specification<User> hasName(String userName){
    if(userName == null || userName.isBlank()){
      return Specification.unrestricted();
    }

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.
            get(USER_FIELD_NAME_FOR_FIRST_NAME), userName);
  }

  public static Specification<User> hasSurname(String userSurname){
    if(userSurname == null || userSurname.isBlank()){
      return Specification.unrestricted();
    }

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.
            get(USER_FIELD_NAME_FOR_SURNAME), userSurname);
  }

  public static Specification<User> withFilters(String userName, String userSurname){
    return Specification.allOf(
        hasName(userName),
        hasSurname(userSurname)
        );
  }
}
