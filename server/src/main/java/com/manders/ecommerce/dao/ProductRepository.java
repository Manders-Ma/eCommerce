package com.manders.ecommerce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manders.ecommerce.entity.Product;
import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  
  @Query(value = "select p from Product p where p.category.id=:id", nativeQuery = false)
  Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);
  
  @Query(value = "select p from Product p where p.name like concat('%', :name, '%')", nativeQuery = false)
  Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
  
  @Modifying
  @Transactional
  @Query(value = "update Product p set p.unitsInStock = p.unitsInStock - :quantity where p.id=:id and p.active=true", nativeQuery = false)
  int reserveInventory(@Param("id") Long id, @Param("quantity") int quantity);
}
