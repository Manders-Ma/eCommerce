package com.manders.ecommerce.dto;

import java.util.List;
import com.manders.ecommerce.entity.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShippingAddressResponse {
  private Embedded embedded;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Embedded {
    private List<ShippingAddress> shippingAddress;
  }
}

