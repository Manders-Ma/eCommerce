package com.example.orderservice.controller;

import com.example.orderservice.dto.request.Purchase;
import com.example.orderservice.dto.response.PurchaseResponse;
import com.example.orderservice.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
  
  @Autowired
  private CheckoutService checkoutService;
  
  @PostMapping("/purchase")
  public ResponseEntity<PurchaseResponse> placeOrder(@RequestBody Purchase purchase) {
    
    ResponseEntity<PurchaseResponse> purchaseResponse = checkoutService.placeOrder(purchase);
    
    return purchaseResponse;
  }
  
}
