package com.example.productservice.dto.response;

import com.example.productservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductPageResponse {
  private Embedded embedded;
  private PageMetadata page;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Embedded {
    private List<Product> products;
  }
}

