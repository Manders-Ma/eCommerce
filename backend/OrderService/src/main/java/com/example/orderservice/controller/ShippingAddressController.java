package com.example.orderservice.controller;

import com.example.orderservice.dto.response.ShippingAddressResponse;
import com.example.orderservice.service.ShippingAddressQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

