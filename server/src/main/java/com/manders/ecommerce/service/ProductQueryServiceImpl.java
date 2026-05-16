package com.manders.ecommerce.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.dao.ProductCategoryRepository;
import com.manders.ecommerce.dao.ProductRepository;
import com.manders.ecommerce.dto.PageMetadata;
import com.manders.ecommerce.dto.ProductPageResponse;
import com.manders.ecommerce.dto.ProductCategoryResponse;
import com.manders.ecommerce.entity.Product;
import com.manders.ecommerce.entity.ProductCategory;

@Service
public class ProductQueryServiceImpl implements ProductQueryService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  @Override
  public ProductPageResponse getProductsByCategory(Long categoryId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

    PageMetadata pageMetadata = new PageMetadata(
      size,
      productPage.getTotalElements(),
      productPage.getTotalPages(),
      page
    );

    ProductPageResponse response = new ProductPageResponse(
      new ProductPageResponse.Embedded(productPage.getContent()),
      pageMetadata
    );

    return response;
  }

  @Override
  public ProductPageResponse searchProductsByName(String name, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> productPage = productRepository.findByNameContaining(name, pageable);

    PageMetadata pageMetadata = new PageMetadata(
      size,
      productPage.getTotalElements(),
      productPage.getTotalPages(),
      page
    );

    ProductPageResponse response = new ProductPageResponse(
      new ProductPageResponse.Embedded(productPage.getContent()),
      pageMetadata
    );

    return response;
  }

  @Override
  public ProductCategoryResponse getAllCategories() {
    List<ProductCategory> categories = productCategoryRepository.findAll();
    ProductCategoryResponse response = new ProductCategoryResponse(
      new ProductCategoryResponse.Embedded(categories)
    );

    return response;
  }
}

