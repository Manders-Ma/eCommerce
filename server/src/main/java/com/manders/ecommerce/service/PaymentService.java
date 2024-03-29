package com.manders.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {
  String sendRequestAPI(String orderTrackngNumber);
  void sendConfirmAPI(String transactionId, String orderTrackingNumber) throws JsonProcessingException;
}
