package com.andrei.ecommerce_innowise.mappers;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy =
    NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentCardMapper {

  PaymentCardMapper Instance = Mappers.getMapper(PaymentCardMapper.class);

  PaymentCard paymentCardDtoToPaymentCard(CreatePaymentCardRequest paymentCardDto);

  PaymentCardResponse paymentCardToPaymentCardResponse(PaymentCard paymentCard);

  PaymentCard updatePaymentCardFromPaymentCardUpdateRequest(
      UpdatePaymentCardRequest paymentCardUpdateRequest,
      @MappingTarget PaymentCard paymentCard
  );
}
