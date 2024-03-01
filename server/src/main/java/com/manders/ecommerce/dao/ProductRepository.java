package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manders.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  
}
