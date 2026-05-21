package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductPackageForm {
    private final String id;
    private final String name;
    private final int amount;
    private final List<ProductForm> products;
}
