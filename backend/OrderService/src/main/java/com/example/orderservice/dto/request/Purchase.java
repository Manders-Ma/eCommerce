package com.example.orderservice.dto.request;

import com.example.orderservice.entity.*;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
  
  private Member member;
  private Customer customer;
  private ShippingAddress shippingAddress;
  private Order order;
  private Set<OrderItem> orderItems;
  
}
