package com.manders.ecommerce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.manders.ecommerce.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
  
  Page<Product> findByCategoryId(Long id, Pageable pageable);
  
  Page<Product> findByNameContaining(String name, Pageable pageable);
  
  @Modifying
  @Query(value = "update Product p set p.unitsInStock = p.unitsInStock - :quantity where p.id=:id and p.active=true and p.unitsInStock >= :quantity", nativeQuery = false)
  int reserveInventory(@Param("id") Long id, @Param("quantity") int quantity);
}
