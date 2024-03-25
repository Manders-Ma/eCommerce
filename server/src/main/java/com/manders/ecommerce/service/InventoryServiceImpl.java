package com.manders.ecommerce.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manders.ecommerce.dao.ProductRepository;
import com.manders.ecommerce.dto.ProductCreation;
import com.manders.ecommerce.entity.OrderItem;
import com.manders.ecommerce.entity.Product;
import com.manders.ecommerce.entity.ProductCategory;

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

  @Override
  @Transactional
  public void updateProduct(Product product) {
    Product productFromDB = productRepository.findById(product.getId()).get();
    
    // 如果已經先存在於DB，代表是做更新動作而已，所以從DB拿出來後，拿前端的資料來更新它。
    productFromDB.setName(product.getName());
    productFromDB.setUnitPrice(product.getUnitPrice());
    productFromDB.setUnitsInStock(product.getUnitsInStock());
    productFromDB.setDescription(product.getDescription());
    productRepository.save(productFromDB);
  }

  @Override
  @Transactional
  public void saveProduct(ProductCreation productCreation) {
    Product product = productCreation.getProduct();
    ProductCategory productCategory = productCreation.getProductCategory();
    product.setCategory(productCategory);
    productRepository.save(product);
  }

}
