package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

@Data
public class RedirectUrls {
  private String confirmUrl;
  private String cancelUrl;
}
