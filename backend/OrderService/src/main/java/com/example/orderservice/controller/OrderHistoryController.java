package com.example.orderservice.controller;

import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderHistoryController {
  @Autowired
  private OrderHistoryService orderHistoryService;
  
  @GetMapping("/order-history")
  public List<Order> getOrderHistory(Authentication authentication) {
    List<Order> orderHistories = orderHistoryService.getOrderHistory(authentication.getName());
    
    if (orderHistories != null) {
      return orderHistories;
    }
    
    return null;
  }
}
