package com.manders.ecommerce.dto;

import java.util.List;
import com.manders.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductPageResponse {
  private Embedded _embedded;
  private PageMetadata page;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Embedded {
    private List<Product> products;
  }
}

