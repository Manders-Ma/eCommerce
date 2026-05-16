package com.manders.ecommerce.service;

import com.manders.ecommerce.dto.ProductPageResponse;
import com.manders.ecommerce.dto.ProductCategoryResponse;
import com.manders.ecommerce.entity.Product;

public interface ProductQueryService {

  Product getProductById(Long id);

  ProductPageResponse getProductsByCategory(Long categoryId, int page, int size);

  ProductPageResponse searchProductsByName(String name, int page, int size);

  ProductCategoryResponse getAllCategories();
}

