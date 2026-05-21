package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPackageForm {
  private String id;
  private String name;
  private int amount;
  private List<ProductForm> products;
  
  public void add(ProductForm product) {
    if (this.products == null) {
      this.products = new ArrayList<>();
    }
    
    this.products.add(product);
  }
  
}
