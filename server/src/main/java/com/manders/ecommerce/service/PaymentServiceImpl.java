package com.manders.ecommerce.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manders.ecommerce.constants.OrderStatusConstants;
import com.manders.ecommerce.constants.PaymentConstants;
import com.manders.ecommerce.dao.OrderRepository;
import com.manders.ecommerce.entity.Order;
import com.manders.ecommerce.entity.OrderItem;
import com.manders.ecommerce.paymentDto.CheckoutPaymentRequestForm;
import com.manders.ecommerce.paymentDto.ConfirmData;
import com.manders.ecommerce.paymentDto.ProductForm;
import com.manders.ecommerce.paymentDto.ProductPackageForm;
import com.manders.ecommerce.paymentDto.RedirectUrls;
import com.manders.ecommerce.util.PaymentUtil;

@Service
public class PaymentServiceImpl implements PaymentService {
  
  @Autowired
  private PaymentUtil paymentUtil;
  
  @Autowired
  private OrderRepository orderRepository;
  
  private String channelId = PaymentConstants.ChannelId;
  private String channelSecret = PaymentConstants.ChannelSecret;
  private String paymentBaseUrl = PaymentConstants.PaymentBaseUrl;
  private String requestUri = PaymentConstants.RequestUri;
  private String baseUri = PaymentConstants.BaseUri;

  @Override
  public String sendRequestAPI(String orderTrackingNumber) {
    Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
    
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
      String body = mapper.writeValueAsString(form);
      String signature = generateSignature(channelSecret, 
          channelSecret + requestUri + body + orderTrackingNumber);
      
      JsonNode response = paymentUtil.sendPostAPI(channelId, orderTrackingNumber, signature, paymentBaseUrl + requestUri, body);
      url = response.get("info").get("paymentUrl").get("web").asText();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    
    return url;
  }
  
  @Override
  public void sendConfirmAPI(String transactionId, String orderTrackingNumber) throws JsonProcessingException {
    Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
    
    ConfirmData confirmData = new ConfirmData();
    confirmData.setAmount(order.getTotalPrice());
    confirmData.setCurrency("TWD");
    ObjectMapper mapper = new ObjectMapper();
    String confirmUri = baseUri + "/" + transactionId + "/confirm";
    
    String body = mapper.writeValueAsString(confirmData);
    String signature = generateSignature(channelSecret, 
        channelSecret + confirmUri + body + orderTrackingNumber);
    JsonNode response = paymentUtil.sendPostAPI(channelId, orderTrackingNumber, signature, paymentBaseUrl + confirmUri, body);
    order.setStatus(OrderStatusConstants.STATUS_TWO);
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
