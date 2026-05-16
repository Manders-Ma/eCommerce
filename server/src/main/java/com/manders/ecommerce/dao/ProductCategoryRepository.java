package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manders.ecommerce.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

}
