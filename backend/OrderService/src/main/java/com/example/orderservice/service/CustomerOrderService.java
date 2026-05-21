package com.example.orderservice.service;


import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Member;
import com.example.orderservice.entity.Order;

public interface CustomerOrderService {
  void saveOrder(Customer customer, Member member, Order order);
}
