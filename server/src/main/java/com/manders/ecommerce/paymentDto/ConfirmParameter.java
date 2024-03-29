package com.manders.ecommerce.paymentDto;

import lombok.Data;

@Data
public class ConfirmParameter {
  private String transactionId;
  private String orderTrackingNumber;
}
