package com.manders.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manders.ecommerce.dto.Purchase;
import com.manders.ecommerce.dto.PurchaseResponse;
import com.manders.ecommerce.service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  
  @Autowired
  private CheckoutService checkoutService;
  
  @PostMapping("/purchase")
  public ResponseEntity<PurchaseResponse> placeOrder(@RequestBody Purchase purchase) {
    
    ResponseEntity<PurchaseResponse> purchaseResponse = checkoutService.placeOrder(purchase);
    
    return purchaseResponse;
  }
  
}
