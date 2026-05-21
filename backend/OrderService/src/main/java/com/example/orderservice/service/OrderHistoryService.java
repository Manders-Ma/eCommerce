package com.example.orderservice.service;


import com.example.orderservice.entity.Order;

import java.util.List;

public interface OrderHistoryService {
  List<Order> getOrderHistory(String email);
}
