package com.manders.ecommerce.dto;

import lombok.Data;

@Data
public class PurchaseResponse {
  private String orderTrackingNumber;
  
  public PurchaseResponse(String orderTrackingNumber) {
    this.orderTrackingNumber = orderTrackingNumber;
  }
}
