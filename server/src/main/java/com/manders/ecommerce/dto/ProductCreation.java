package com.manders.ecommerce.dto;

import com.manders.ecommerce.entity.Product;
import com.manders.ecommerce.entity.ProductCategory;
import lombok.Data;


@Data
public class ProductCreation {
  private Product product;
  private ProductCategory productCategory;
}
