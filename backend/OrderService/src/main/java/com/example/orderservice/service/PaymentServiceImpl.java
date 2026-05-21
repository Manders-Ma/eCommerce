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
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentUtil paymentUtil;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final LinePayConfig linePayConfig;

    public PaymentServiceImpl(PaymentUtil paymentUtil,
                              OrderRepository orderRepository,
                              ObjectMapper objectMapper,
                              LinePayConfig linePayConfig) {
        this.paymentUtil = paymentUtil;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.linePayConfig = linePayConfig;
    }

    @Override
    public String sendRequestAPI(String orderTrackingNumber) {
        Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderTrackingNumber);
        }

        List<ProductForm> products = order.getOrderItems().stream()
                .map(item -> ProductForm.builder()
                        .name(item.getName())
                        .price(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        ProductPackageForm productPackage = ProductPackageForm.builder()
                .id("package")
                .name("ecommerce")
                .amount(order.getTotalPrice())
                .products(products)
                .build();

        CheckoutPaymentRequestForm form = CheckoutPaymentRequestForm.builder()
                .amount(order.getTotalPrice())
                .currency("TWD")
                .orderId(orderTrackingNumber)
                .packages(List.of(productPackage))
                .redirectUrls(RedirectUrls.builder()
                        .confirmUrl("http://localhost:4200/confirm-pay")
                        .cancelUrl("")
                        .build())
                .build();

        try {
            String nonce = UUID.randomUUID().toString();
            String body = objectMapper.writeValueAsString(form);
            String signature = generateSignature(
                    linePayConfig.getChannelSecret(),
                    linePayConfig.getChannelSecret() + linePayConfig.getRequestUri() + body + nonce);

            JsonNode response = paymentUtil.sendPostAPI(
                    linePayConfig.getChannelId(), nonce, signature,
                    linePayConfig.getBaseUrl() + linePayConfig.getRequestUri(), body);

            JsonNode paymentUrl = response.path("info").path("paymentUrl").path("web");
            if (paymentUrl.isMissingNode() || paymentUrl.isNull() || paymentUrl.asText().isBlank()) {
                String returnCode = response.path("returnCode").asText("unknown");
                String returnMessage = response.path("returnMessage").asText("Payment API did not return a web payment URL");
                throw new IllegalStateException("Payment API request failed: " + returnCode + " - " + returnMessage);
            }
            return paymentUrl.asText();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize payment request", e);
        }
    }

    @Override
    public void sendConfirmAPI(String transactionId, String orderTrackingNumber) throws JsonProcessingException {
        Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderTrackingNumber);
        }

        ConfirmData confirmData = ConfirmData.builder()
                .amount(order.getTotalPrice())
                .currency("TWD")
                .build();

        String confirmUri = linePayConfig.getBaseUri() + "/" + transactionId + "/confirm";
        String body = objectMapper.writeValueAsString(confirmData);
        String nonce = UUID.randomUUID().toString();
        String signature = generateSignature(
                linePayConfig.getChannelSecret(),
                linePayConfig.getChannelSecret() + confirmUri + body + nonce);

        JsonNode response = paymentUtil.sendPostAPI(
                linePayConfig.getChannelId(), nonce, signature,
                linePayConfig.getBaseUrl() + confirmUri, body);

        if (!"0000".equals(response.path("returnCode").asText())) {
            String returnCode = response.path("returnCode").asText("unknown");
            String returnMessage = response.path("returnMessage").asText("Payment API confirm failed");
            throw new IllegalStateException("Payment API confirm failed: " + returnCode + " - " + returnMessage);
        }
        order.setStatus(OrderStatus.PAYMENT_COMPLETED.getStatus());
        orderRepository.save(order);
    }

    private String generateSignature(String channelSecret, String data) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            sha256HMAC.init(new SecretKeySpec(channelSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Failed to generate payment signature", e);
        }
    }
}
