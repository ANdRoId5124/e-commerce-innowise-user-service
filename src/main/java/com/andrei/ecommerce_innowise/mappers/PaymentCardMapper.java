package com.andrei.ecommerce_innowise.mappers;

import com.andrei.ecommerce_innowise.DTO.PaymentCardDto;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy =
    NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentCardMapper {

  PaymentCardMapper Instance = Mappers.getMapper(PaymentCard.class);

  PaymentCard paymentCardDtoToPaymentCard(PaymentCardDto paymentCardDto);

  PaymentCardDto paymentCardToPaymentDto(PaymentCard paymentCard);

  PaymentCard updatePaymentCardFromPaymentCardDto(
      PaymentCardDto paymentCardDto,
      @MappingTarget PaymentCard paymentCard);
}
