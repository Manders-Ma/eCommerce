package com.example.orderservice.service;

import com.example.orderservice.dto.request.Purchase;
import com.example.orderservice.dto.response.PurchaseResponse;
import org.springframework.http.ResponseEntity;

public interface CheckoutService {
  
  ResponseEntity<PurchaseResponse> placeOrder(Purchase purchase);
}
