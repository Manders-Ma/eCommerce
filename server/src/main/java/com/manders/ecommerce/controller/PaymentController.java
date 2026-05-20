package com.manders.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.manders.ecommerce.dto.MessageResponse;
import com.manders.ecommerce.paymentDto.ConfirmParameter;
import com.manders.ecommerce.service.PaymentService;

@RestController
@RequestMapping("/pay")
public class PaymentController {
  
  @Autowired
  private PaymentService paymentService;
  
  @PostMapping("/request")
  public ResponseEntity<MessageResponse> request(@RequestBody String orderTrackingNumber) {
    String url = paymentService.sendRequestAPI(orderTrackingNumber);
    
    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(url));
  }
  
  @PostMapping("/confirm")
  public ResponseEntity<MessageResponse> confirm(@RequestBody ConfirmParameter parameter) {
    ResponseEntity<MessageResponse> response = null;
    try {
      paymentService.sendConfirmAPI(parameter.getTransactionId(), parameter.getOrderTrackingNumber());
      response = ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success"));
    } catch (JsonProcessingException | IllegalStateException e) {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("failed"));
    }
    
    return response;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<MessageResponse> handleBadRequest(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<MessageResponse> handlePaymentError(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new MessageResponse(e.getMessage()));
  }
}
