package com.example.orderservice.infrastructure.payment.linepay.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PaymentUtil(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode sendPostAPI(String channelId, String nonce, String signature, String url, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", channelId);
        headers.set("X-LINE-Authorization-Nonce", nonce);
        headers.set("X-LINE-Authorization", signature);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String response;
        try {
            response = restTemplate.postForObject(url, request, String.class);
        } catch (RestClientResponseException e) {
            response = e.getResponseBodyAsString();
        }

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse payment API response", e);
        }
    }
}
