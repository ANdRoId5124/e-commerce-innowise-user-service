package com.andrei.ecommerce_innowise.contoroller;

import com.andrei.ecommerce_innowise.DTO.request.payment_card.CreatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.request.payment_card.UpdatePaymentCardRequest;
import com.andrei.ecommerce_innowise.DTO.response.payment_card.PaymentCardResponse;
import com.andrei.ecommerce_innowise.entities.PaymentCard;
import com.andrei.ecommerce_innowise.service.PaymentCardService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment-cards")
public class PaymentCardController {
  private final PaymentCardService paymentCardService;
  private static final int PAGE_INDEX_OFFSET = 1;

  public PaymentCardController(PaymentCardService paymentCardService) {
    this.paymentCardService = paymentCardService;
  }

  @PostMapping("{id}")
  public ResponseEntity<PaymentCardResponse> createCard(
      @Valid @RequestBody CreatePaymentCardRequest createPaymentCardRequest,
      @PathVariable Long id
      ){

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
        paymentCardService
            .createCard(
                createPaymentCardRequest,
                id
            )
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteCard(@PathVariable Long id){
    paymentCardService.deleteCard(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("{id}")
  public ResponseEntity<PaymentCardResponse> updateCard(
      @Valid @RequestBody UpdatePaymentCardRequest updatePaymentCardRequest,
      @PathVariable Long id
  ){
    return ResponseEntity.ok(
        paymentCardService.
            updateCard(
                updatePaymentCardRequest,
                id
            )
    );
  }

  @PatchMapping("activation/{id}")
  public ResponseEntity<Void> activatePaymentCard(
      @PathVariable Long id
  ){
    paymentCardService.activateCard(paymentCardService.getCardById(id));
    return ResponseEntity.ok().build();
  }

  @PatchMapping("deactivation/{id}")
  public ResponseEntity<Void> deactivatePaymentCard(
      @PathVariable Long id
  ){
    paymentCardService.deactivateCard(paymentCardService.getCardById(id));
    return ResponseEntity.ok().build();
  }

  @GetMapping("{id}")
  public ResponseEntity<PaymentCard> getCardById(
      @PathVariable Long id
  ){

    return ResponseEntity.ok(
            paymentCardService
                .getCardById(id)
        );
  }

  @GetMapping("all")
  public ResponseEntity<List<PaymentCard>> fetchAllPaymentCardsFiltered(
      @RequestParam(name = "page-number", required = false, defaultValue = "1") int pageNumber,
      @RequestParam(name = "page-size", required = false, defaultValue = "10") int pageSize,
      @RequestParam(name = "name", required = false) String userName,
      @RequestParam(name = "surname", required = false) String userSurname
  ){
    return ResponseEntity.ok(
        paymentCardService.
            fetchAll(
                pageNumber - PAGE_INDEX_OFFSET,
                pageSize,
                userName,
                userSurname
                )
    );
  }

  @GetMapping("users/{userId}")
  public ResponseEntity<List<PaymentCard>> getAllUserCardsByUserId(
      @PathVariable Long userId
  ){
    return ResponseEntity.ok(
        paymentCardService
            .getAllCardsByUserId(userId)
    );
  }
}
