package com.manders.ecommerce.paymentDto;

import lombok.Data;

@Data
public class RedirectUrls {
  private String confirmUrl;
  private String cancelUrl;
}
