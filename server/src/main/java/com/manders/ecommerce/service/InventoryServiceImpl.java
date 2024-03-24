package com.manders.ecommerce.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manders.ecommerce.dao.ProductRepository;
import com.manders.ecommerce.entity.OrderItem;

@Service
public class InventoryServiceImpl implements InventoryService {
  
  @Autowired
  private ProductRepository productRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void reserveInventory(Set<OrderItem> orderItems) {
    orderItems.forEach(item -> productRepository.reserveInventory(item.getProductId(), item.getQuantity()));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteProductById(Long id) {
    productRepository.deleteById(id);
  }

}
