package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

@Data
public class ConfirmData {
  private int amount;
  private String currency;
}
