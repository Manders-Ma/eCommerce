package com.manders.ecommerce.service;

import java.util.Set;
import com.manders.ecommerce.entity.OrderItem;

public interface InventoryService {
  void reserveInventory(Set<OrderItem> orderItems);
  
  void deleteProductById(Long id);
}
