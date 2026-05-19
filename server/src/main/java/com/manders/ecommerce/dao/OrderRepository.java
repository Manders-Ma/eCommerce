package com.manders.ecommerce.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manders.ecommerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
  
  Order findByOrderTrackingNumber(String orderTrackingNumber);
  
  List<Order> findByCustomerMemberEmailOrderByDateCreatedDesc(String email);
}
