package com.example.productservice.service;


import com.example.productservice.dto.response.ProductCategoryResponse;
import com.example.productservice.dto.response.ProductPageResponse;
import com.example.productservice.entity.Product;

public interface ProductQueryService {

  Product getProductById(Long id);

  ProductPageResponse getProductsByCategory(Long categoryId, int page, int size);

  ProductPageResponse searchProductsByName(String name, int page, int size);

  ProductCategoryResponse getAllCategories();
}


