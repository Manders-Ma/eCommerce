package com.manders.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.manders.ecommerce.dto.ProductPageResponse;
import com.manders.ecommerce.entity.Product;
import com.manders.ecommerce.service.ProductQueryService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductQueryService productQueryService;

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable(name = "id") Long id) {
    Product product = productQueryService.getProductById(id);
    if (product == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(product);
  }

  @GetMapping("/search/findByCategoryId")
  public ResponseEntity<ProductPageResponse> getProductsByCategory(
    @RequestParam(name = "id") Long categoryId,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size) {

    ProductPageResponse response = productQueryService.getProductsByCategory(categoryId, page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search/findByNameContaining")
  public ResponseEntity<ProductPageResponse> searchProductsByName(
    @RequestParam(name = "name") String name,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size) {

    ProductPageResponse response = productQueryService.searchProductsByName(name, page, size);
    return ResponseEntity.ok(response);
  }
}

