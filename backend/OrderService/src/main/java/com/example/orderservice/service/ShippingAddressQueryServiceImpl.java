package com.example.orderservice.service;

import com.example.orderservice.dto.response.ShippingAddressResponse;
import com.example.orderservice.entity.ShippingAddress;
import com.example.orderservice.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingAddressQueryServiceImpl implements ShippingAddressQueryService {

  @Autowired
  private ShippingAddressRepository shippingAddressRepository;

  @Override
  public ShippingAddressResponse getAllShippingAddresses() {
    List<ShippingAddress> addresses = shippingAddressRepository.findAll();
    ShippingAddressResponse response = new ShippingAddressResponse(
      new ShippingAddressResponse.Embedded(addresses)
    );

    return response;
  }
}

