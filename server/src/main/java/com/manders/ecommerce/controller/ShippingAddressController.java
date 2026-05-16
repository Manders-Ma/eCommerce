package com.manders.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manders.ecommerce.dto.ShippingAddressResponse;
import com.manders.ecommerce.service.ShippingAddressQueryService;

@RestController
@RequestMapping("/shipping-address")
public class ShippingAddressController {

  @Autowired
  private ShippingAddressQueryService shippingAddressQueryService;

  @GetMapping
  public ResponseEntity<ShippingAddressResponse> getAllShippingAddresses() {
    ShippingAddressResponse response = shippingAddressQueryService.getAllShippingAddresses();
    return ResponseEntity.ok(response);
  }
}

