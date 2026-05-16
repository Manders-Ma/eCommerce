package com.manders.ecommerce.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.dao.ShippingAddressRepository;
import com.manders.ecommerce.dto.ShippingAddressResponse;
import com.manders.ecommerce.entity.ShippingAddress;

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

