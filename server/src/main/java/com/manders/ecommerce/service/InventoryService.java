package com.manders.ecommerce.service;

import java.util.Set;
import com.manders.ecommerce.entity.OrderItem;
import com.manders.ecommerce.entity.Product;

public interface InventoryService {
  void reserveInventory(Set<OrderItem> orderItems);
  
  void deleteProductById(Long id);
  
  void updateProduct(Product product);
}
