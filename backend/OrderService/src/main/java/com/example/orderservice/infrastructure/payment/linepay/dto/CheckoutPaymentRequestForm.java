package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutPaymentRequestForm {
  private int amount;
  private String currency;
  private String orderId;
  private List<ProductPackageForm> packages;
  private RedirectUrls redirectUrls;
}
