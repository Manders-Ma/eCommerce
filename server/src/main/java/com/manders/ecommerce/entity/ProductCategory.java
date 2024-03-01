package com.manders.ecommerce.entity;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_category")
public class ProductCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
  @Column(name = "category_name")
  private String categoryName;
  
  
  // [Q] What is CascadeType.ALL?
  // [A] For example, consider a scenario where you have a Customer entity with a one-to-many relationship to Order entities. 
  // By using CascadeType.ALL, any operation performed on the Customer entity (such as persist, merge, remove, or refresh) 
  // will also be propagated to all associated Order entities.
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
  private Set<Product> products;
}
