package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
  
  @Autowired
  private OrderRepository orderRepository;

  @Override
  @Transactional
  public List<Order> getOrderHistory(String email) {
    List<Order> orderHistories = orderRepository.findByCustomerMemberEmailOrderByDateCreatedDesc(email);
    
    return orderHistories;
  }

}
