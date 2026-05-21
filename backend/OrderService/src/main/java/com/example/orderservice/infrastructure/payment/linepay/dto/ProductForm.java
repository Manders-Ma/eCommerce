package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductForm {
    private final String name;
    private final int quantity;
    private final int price;
}
