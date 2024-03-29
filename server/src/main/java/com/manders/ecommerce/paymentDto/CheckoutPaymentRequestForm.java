package com.manders.ecommerce.paymentDto;

import java.util.List;
import lombok.Data;

@Data
public class CheckoutPaymentRequestForm {
  private int amount;
  private String currency;
  private String orderId;
  private List<ProductPackageForm> packages;
  private RedirectUrls redirectUrls;
}
