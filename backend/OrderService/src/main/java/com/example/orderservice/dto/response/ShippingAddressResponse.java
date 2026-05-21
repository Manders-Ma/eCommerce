package com.example.orderservice.dto.response;

import com.example.orderservice.entity.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

