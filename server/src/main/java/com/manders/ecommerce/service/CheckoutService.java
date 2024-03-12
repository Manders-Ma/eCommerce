package com.manders.ecommerce.service;

import com.manders.ecommerce.dto.Purchase;
import com.manders.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {
  
  PurchaseResponse placeOrder(Purchase purchase);
}
