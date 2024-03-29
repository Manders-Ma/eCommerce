package com.manders.ecommerce.paymentDto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

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
