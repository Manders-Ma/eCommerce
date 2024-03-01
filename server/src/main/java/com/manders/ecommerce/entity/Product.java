package com.manders.ecommerce.entity;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "sku")
  private String sku;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "description")
  private String description;
  
  @Column(name = "unit_price")
  private int unitPrice;
  
  @Column(name = "image_url")
  private String imgUrl;

  @Column(name = "active")
  private boolean active;
  
  @Column(name = "units_in_stock")
  private int unitsInStock;
  
  @Column(name = "date_created")
  @CreationTimestamp
  private Date dateCreated;
  
  @Column(name = "last_updated")
  @UpdateTimestamp
  private Date lastUpdated;
  
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private ProductCategory category;
}
