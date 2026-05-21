package com.example.orderservice.controller;

import com.example.orderservice.dto.response.MessageResponse;
import com.example.orderservice.infrastructure.payment.linepay.dto.ConfirmParameter;
import com.example.orderservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
