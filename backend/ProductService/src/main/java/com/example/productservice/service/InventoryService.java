package com.example.productservice.service;


import com.example.productservice.dto.request.ProductCreation;
import com.example.productservice.entity.OrderItem;
import com.example.productservice.entity.Product;

import java.util.Set;

public interface InventoryService {
  void reserveInventory(Set<OrderItem> orderItems);
  
  void deleteProductById(Long id);
  
  void updateProduct(Product product);
  
  void saveProduct(ProductCreation productCreation);
}
