package com.example.productservice.controller;

import com.example.productservice.dto.response.ProductPageResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
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

