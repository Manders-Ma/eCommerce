package com.example.productservice.dto.response;

import com.example.productservice.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategoryResponse {
  private Embedded embedded;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Embedded {
    private List<ProductCategory> productCategory;
  }
}

