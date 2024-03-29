package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manders.ecommerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
  
  @Query(value = "select o from Order o where o.orderTrackingNumber = :orderTrackingNumber")
  Order findByOrderTrackingNumber(@Param("orderTrackingNumber") String orderTrackingNumber);
}
