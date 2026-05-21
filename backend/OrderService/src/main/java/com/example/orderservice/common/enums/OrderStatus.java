package com.example.orderservice.common.enums;

public enum OrderStatus {
  WAITING_FOR_PAYMENT("等待付款"),
  PAYMENT_COMPLETED("已完成付款");

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
