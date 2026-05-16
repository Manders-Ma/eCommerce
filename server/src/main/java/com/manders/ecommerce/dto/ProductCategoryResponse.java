package com.manders.ecommerce.dto;

import java.util.List;
import com.manders.ecommerce.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategoryResponse {
  private Embedded _embedded;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Embedded {
    private List<ProductCategory> productCategory;
  }
}

