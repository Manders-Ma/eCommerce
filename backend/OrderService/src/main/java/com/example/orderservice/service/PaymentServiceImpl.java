package com.example.orderservice.service;

import com.example.orderservice.common.enums.OrderStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.infrastructure.payment.linepay.config.LinePayConfig;
import com.example.orderservice.infrastructure.payment.linepay.dto.*;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.infrastructure.payment.linepay.client.PaymentUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
  
  @Autowired
  private PaymentUtil paymentUtil;
  
  @Autowired
  private OrderRepository orderRepository;
  
  private String channelId = LinePayConfig.ChannelId;
  private String channelSecret = LinePayConfig.ChannelSecret;
  private String paymentBaseUrl = LinePayConfig.PaymentBaseUrl;
  private String requestUri = LinePayConfig.RequestUri;
  private String baseUri = LinePayConfig.BaseUri;

  @Override
  public String sendRequestAPI(String orderTrackingNumber) {
    Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
    if (order == null) {
      throw new IllegalArgumentException("Order not found: " + orderTrackingNumber);
    }
    
    CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

    form.setAmount(order.getTotalPrice());
    form.setCurrency("TWD");
    form.setOrderId(orderTrackingNumber);

    ProductPackageForm productPackageForm = new ProductPackageForm();
    productPackageForm.setId("package");
    productPackageForm.setName("ecommerce");
    productPackageForm.setAmount(order.getTotalPrice());
    
    for(OrderItem orderItem: order.getOrderItems()) {
      ProductForm productForm = new ProductForm();
      productForm.setName(orderItem.getName());
      productForm.setPrice(orderItem.getUnitPrice());
      productForm.setQuantity(orderItem.getQuantity());
      productPackageForm.add(productForm);
    }

    form.setPackages(Collections.singletonList(productPackageForm));
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setConfirmUrl("http://localhost:4200/confirm-pay");
    redirectUrls.setCancelUrl("");
    form.setRedirectUrls(redirectUrls);
    
    ObjectMapper mapper = new ObjectMapper();
    String url = "";
    try {
      String nonce = UUID.randomUUID().toString();
      String body = mapper.writeValueAsString(form);
      String signature = generateSignature(channelSecret, 
          channelSecret + requestUri + body + nonce);
      
      JsonNode response = paymentUtil.sendPostAPI(channelId, nonce, signature, paymentBaseUrl + requestUri, body);
      JsonNode paymentUrl = response.path("info").path("paymentUrl").path("web");
      if (paymentUrl.isMissingNode() || paymentUrl.isNull() || paymentUrl.asText().isBlank()) {
        String returnCode = response.path("returnCode").asText("unknown");
        String returnMessage = response.path("returnMessage").asText("Payment API did not return a web payment URL");
        throw new IllegalStateException("Payment API request failed: " + returnCode + " - " + returnMessage);
      }
      url = paymentUrl.asText();
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Unable to serialize payment request", e);
    }
    
    return url;
  }
  
  @Override
  public void sendConfirmAPI(String transactionId, String orderTrackingNumber) throws JsonProcessingException {
    Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
    if (order == null) {
      throw new IllegalArgumentException("Order not found: " + orderTrackingNumber);
    }
    
    ConfirmData confirmData = new ConfirmData();
    confirmData.setAmount(order.getTotalPrice());
    confirmData.setCurrency("TWD");
    ObjectMapper mapper = new ObjectMapper();
    String confirmUri = baseUri + "/" + transactionId + "/confirm";
    
    String body = mapper.writeValueAsString(confirmData);
    String nonce = UUID.randomUUID().toString();
    String signature = generateSignature(channelSecret, 
        channelSecret + confirmUri + body + nonce);
    JsonNode response = paymentUtil.sendPostAPI(channelId, nonce, signature, paymentBaseUrl + confirmUri, body);
    if (!"0000".equals(response.path("returnCode").asText())) {
      String returnCode = response.path("returnCode").asText("unknown");
      String returnMessage = response.path("returnMessage").asText("Payment API confirm failed");
      throw new IllegalStateException("Payment API confirm failed: " + returnCode + " - " + returnMessage);
    }
    order.setStatus(OrderStatus.PAYMENT_COMPLETED.getStatus());
    orderRepository.save(order);
  }
  
  public String generateSignature(String channelSecret, String data){
    String signature = "";
    try {
      Mac sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(channelSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256HMAC.init(secretKey);
      byte[] hmacBytes = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
      signature = Base64.getEncoder().encodeToString(hmacBytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return signature;
  }
  
}
