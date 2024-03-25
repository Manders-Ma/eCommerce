package com.manders.ecommerce.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.constants.OrderStatusConstants;
import com.manders.ecommerce.dto.Purchase;
import com.manders.ecommerce.dto.PurchaseResponse;
import com.manders.ecommerce.entity.Order;
import com.manders.ecommerce.entity.OrderItem;

@Service
public class CheckoutServiceImpl implements CheckoutService {
  
  @Autowired
  private InventoryService inventoryService;
  
  @Autowired
  private CustomerOrderService customerOrderService;
  
  
  @Override
  public ResponseEntity<PurchaseResponse> placeOrder(Purchase purchase) {
    
    ResponseEntity<PurchaseResponse> response = null;
    String orderTrackingNumber = "";
    
    // retrieve the order and orderItems info from dto
    Order order = purchase.getOrder();
    Set<OrderItem> orderItems = purchase.getOrderItems();
    
    try {
      // 調用管理庫存的方法，有@Transcational
      this.inventoryService.reserveInventory(orderItems);
      
      // populate order with order items
      orderItems.forEach(item -> order.add(item));
      
      // generate tracking number
      orderTrackingNumber = generateOrderTrackingNumber();
      order.setOrderTrackingNumber(orderTrackingNumber);
      
      // populate order with shippingAddressId
      order.setShippingAddressId(purchase.getShippingAddress().getId());
      
      // 設定訂單狀態，已經留完商品數量，剩下就是等付款。
      order.setStatus(OrderStatusConstants.STATUS_ONE);
      
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PurchaseResponse(""));
      return response;
    }
    
    
    try {
      /* 
       * 假設場景是同個member同時下訂單，並填入相同的customer資料，這時會發生幻讀的問題。
       * 我在創建customer table的時候設定了Unique key，打算用唯一性來解決他，直接讓某一個請求失敗。
       * 同個member同時下訂單這場景有點作弊嫌疑，所以失敗不會造成太大影響。
       */
      this.customerOrderService.saveOrder(purchase.getCustomer(), purchase.getMember(), order);
    } catch (Exception e) {
      response = ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PurchaseResponse(""));
      return response;
    }
    
    response = ResponseEntity.status(HttpStatus.OK).body(new PurchaseResponse(orderTrackingNumber));
    
    return response;
  }
  
  private String generateOrderTrackingNumber() {
    
    // generate a random UUID number (we use UUID version4)
    return UUID.randomUUID().toString();
    
  }
}



