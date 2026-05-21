package com.example.orderservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignJwtInterceptor implements RequestInterceptor {

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    @Value("${internal.service.secret}")
    private String internalSecret;

    @Override
    public void apply(RequestTemplate template) {
        template.header(INTERNAL_SECRET_HEADER, internalSecret);
    }
}
