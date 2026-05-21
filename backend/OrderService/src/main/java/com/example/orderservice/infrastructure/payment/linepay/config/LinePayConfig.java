package com.example.orderservice.infrastructure.payment.linepay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "linepay")
@Getter
@Setter
public class LinePayConfig {
    private String channelId;
    private String channelSecret;
    private String baseUrl;
    private String requestUri;
    private String baseUri;
}
