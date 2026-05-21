package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping_address")
@Getter
@Setter
public class ShippingAddress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "address")
  private String address;
}
