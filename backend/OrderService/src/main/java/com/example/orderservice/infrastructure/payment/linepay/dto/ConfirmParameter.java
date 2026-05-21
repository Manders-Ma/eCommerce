package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

@Data
public class ConfirmParameter {
  private String transactionId;
  private String orderTrackingNumber;
}
