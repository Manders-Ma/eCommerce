package com.manders.ecommerce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.manders.ecommerce.entity.Product;

@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long> {
  
  @Query(value = "select p from Product p where p.category.id=:id", nativeQuery = false)
  Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);
  
  @Query(value = "select p from Product p where p.name like concat('%', :name, '%')", nativeQuery = false)
  Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
}
