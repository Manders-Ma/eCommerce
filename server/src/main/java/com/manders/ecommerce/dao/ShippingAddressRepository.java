package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manders.ecommerce.entity.ShippingAddress;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long>{

}
