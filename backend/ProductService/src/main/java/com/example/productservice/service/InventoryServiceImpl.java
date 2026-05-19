package com.example.productservice.service;

import com.example.productservice.dto.request.ProductCreation;
import com.example.productservice.entity.OrderItem;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductCategory;
import com.example.productservice.exception.InsufficientInventoryException;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class InventoryServiceImpl implements InventoryService {
  
  @Autowired
  private ProductRepository productRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void reserveInventory(Set<OrderItem> orderItems) {
    for (OrderItem item : orderItems) {
      int updatedRows = productRepository.reserveInventory(item.getProductId(), item.getQuantity());
      if (updatedRows == 0) {
        throw new InsufficientInventoryException(
            "商品 ID: " + item.getProductId() + " 庫存不足，無法預留"
        );
      }
    }
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.deleteById(id);
  }

  @Override
  public void updateProduct(Product product) {
    Product productFromDB = productRepository.findById(product.getId()).orElse(null);

    if (productFromDB == null) {
      throw new RuntimeException("找不到商品，無法更新。");
    } else {
      if (product.getName() != null) productFromDB.setName(product.getName());
      if (product.getUnitPrice() != null) productFromDB.setUnitPrice(product.getUnitPrice());
      if (product.getUnitsInStock() != null) productFromDB.setUnitsInStock(product.getUnitsInStock());
      if (product.getDescription() != null) productFromDB.setDescription(product.getDescription());
      productRepository.save(productFromDB);
    }
  }

  @Override
  public void saveProduct(ProductCreation productCreation) {
    Product product = productCreation.getProduct();
    ProductCategory productCategory = productCreation.getProductCategory();
    product.setCategory(productCategory);
    productRepository.save(product);
  }

}
