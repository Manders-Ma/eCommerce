package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.manders.ecommerce.entity.ShippingAddress;

@RepositoryRestResource(collectionResourceRel = "shippingAddress", path = "shipping-address")
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long>{

}
