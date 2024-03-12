package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.manders.ecommerce.entity.ShippingAddress;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "shippingAddress", path = "shipping-address")
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long>{

}
