package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "product")
@Getter
@Setter
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
  private Integer unitPrice;
  
  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "active")
  private boolean active;
  
  @Column(name = "units_in_stock")
  private Integer unitsInStock;
  
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
