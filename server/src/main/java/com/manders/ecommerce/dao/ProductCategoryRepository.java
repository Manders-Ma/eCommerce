package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.manders.ecommerce.entity.ProductCategory;

// 由於默認的JSON entry名字以及API路徑是, class 名字加上 s, 若不想使用默認值可以透過以下方法來修改:

// 用 collectionResourceRel 來指定從此API得到的JSON entry的名字
// 用 path 來指定 API 路徑
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

}
