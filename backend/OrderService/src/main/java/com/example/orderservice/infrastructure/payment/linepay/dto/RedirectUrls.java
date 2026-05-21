package com.example.orderservice.infrastructure.payment.linepay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedirectUrls {
    private final String confirmUrl;
    private final String cancelUrl;
}
