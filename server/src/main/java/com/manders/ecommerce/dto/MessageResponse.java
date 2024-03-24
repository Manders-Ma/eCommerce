package com.manders.ecommerce.dto;

import lombok.Data;

@Data
public class MessageResponse {
  private String message;
  
  public MessageResponse(String message) {
    this.message = message;
  }
}
