package com.manders.ecommerce.dto;

import java.util.Set;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Order;
import com.manders.ecommerce.entity.OrderItem;
import com.manders.ecommerce.entity.ShippingAddress;
import lombok.Data;

@Data
public class Purchase {
  
  private Customer customer;
  private ShippingAddress shippingAddress;
  private Order order;
  private Set<OrderItem> orderItems;
  
}
