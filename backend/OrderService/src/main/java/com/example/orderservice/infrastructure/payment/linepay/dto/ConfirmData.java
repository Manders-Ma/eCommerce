package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmData {
    private final int amount;
    private final String currency;
}
