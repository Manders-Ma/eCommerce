package com.manders.ecommerce.service;

import java.util.List;
import com.manders.ecommerce.entity.Order;

public interface OrderHistoryService {
  List<Order> getOrderHistory(String email);
}
