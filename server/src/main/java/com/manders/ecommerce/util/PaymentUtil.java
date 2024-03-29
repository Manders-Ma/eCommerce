package com.manders.ecommerce.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PaymentUtil {
  public JsonNode sendPostAPI(String channelId, String nonce, String signature, String url, String body) {
    RestTemplate restTemplate = new RestTemplate();
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("X-LINE-ChannelId", channelId);
    headers.add("X-LINE-Authorization-Nonce", nonce);
    headers.add("X-LINE-Authorization", signature);
    
    HttpEntity<String> request = new HttpEntity<String>(body, headers);
    String response = restTemplate.postForObject(url, request, String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = null;
    
    try {
      json = mapper.readTree(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return json;
  }
}
