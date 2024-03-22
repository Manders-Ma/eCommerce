package com.manders.ecommerce.service;

import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;
import com.manders.ecommerce.entity.Order;

public interface CustomerOrderService {
  void saveOrder(Customer customer, Member member, Order order);
}
