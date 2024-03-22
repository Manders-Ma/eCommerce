package com.manders.ecommerce.service;

import org.springframework.http.ResponseEntity;
import com.manders.ecommerce.dto.Purchase;
import com.manders.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {
  
  ResponseEntity<PurchaseResponse> placeOrder(Purchase purchase);
}
