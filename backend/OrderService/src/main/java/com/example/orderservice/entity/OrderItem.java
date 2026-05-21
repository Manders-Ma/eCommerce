package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "image_url")
  private String imageUrl;
  
  @Column(name = "quantity")
  private int quantity;
  
  @Column(name = "unit_price")
  private int unitPrice;
  
  @Column(name = "product_id")
  private Long productId;
  
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
}



