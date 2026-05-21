package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CheckoutPaymentRequestForm {
    private final int amount;
    private final String currency;
    private final String orderId;
    private final List<ProductPackageForm> packages;
    private final RedirectUrls redirectUrls;
}
