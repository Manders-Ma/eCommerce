package com.manders.ecommerce.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manders.ecommerce.dao.OrderRepository;
import com.manders.ecommerce.entity.Order;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
  
  @Autowired
  private OrderRepository orderRepository;

  @Override
  @Transactional
  public List<Order> getOrderHistory(String email) {
    List<Order> orderHistories = orderRepository.findByMemberEmailOrderByDateDesc(email);
    
    return orderHistories;
  }

}
