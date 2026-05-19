package com.example.productservice.controller;

import com.example.productservice.dto.response.ProductCategoryResponse;
import com.example.productservice.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

  @Autowired
  private ProductQueryService productQueryService;

  @GetMapping
  public ResponseEntity<ProductCategoryResponse> getAllProductCategories() {
    ProductCategoryResponse response = productQueryService.getAllCategories();
    return ResponseEntity.ok(response);
  }
}

