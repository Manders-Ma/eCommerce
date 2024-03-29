package com.manders.ecommerce.paymentDto;

import lombok.Data;

@Data
public class ConfirmData {
  private int amount;
  private String currency;
}
