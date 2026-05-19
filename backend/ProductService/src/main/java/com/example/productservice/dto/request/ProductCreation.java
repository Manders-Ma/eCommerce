package com.example.productservice.dto.request;

import com.example.productservice.entity.Product;
import com.example.productservice.entity.ProductCategory;
import lombok.Data;


@Data
public class ProductCreation {
  private Product product;
  private ProductCategory productCategory;
}
