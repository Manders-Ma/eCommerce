package com.manders.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageMetadata {
  private int size;
  private long totalElements;
  private int totalPages;
  private int number;
}

