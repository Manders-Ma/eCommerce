package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Data;

@Data
public class ProductForm {
  private String name;
  private int quantity;
  private int price;
  
}
