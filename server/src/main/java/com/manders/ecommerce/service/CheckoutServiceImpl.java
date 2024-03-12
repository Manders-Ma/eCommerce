package com.manders.ecommerce.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.dao.CustomerRepository;
import com.manders.ecommerce.dto.Purchase;
import com.manders.ecommerce.dto.PurchaseResponse;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Order;
import com.manders.ecommerce.entity.OrderItem;
import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {
  
  @Autowired
  private CustomerRepository customerRepository;
  
  
  @Override
  @Transactional
  public PurchaseResponse placeOrder(Purchase purchase) {
    
    // retrieve the order info from dto
    Order order = purchase.getOrder();
    
    // generate tracking number
    String orderTrackingNumber = generateOrderTrackingNumber();
    order.setOrderTrackingNumber(orderTrackingNumber);
    
    // populate order with orderItems
    Set<OrderItem> orderItems = purchase.getOrderItems();
    orderItems.forEach(item -> order.add(item));
    
    // populate order with shippingAddressId
    order.setShippingAddressId(purchase.getShippingAddress().getId());
    
    // populate customer with order
    Customer customerFromDB = customerRepository.findCustomer(
        purchase.getCustomer().getFirstName(), 
        purchase.getCustomer().getLastName(), 
        purchase.getCustomer().getEmail()
    );
    Customer customer;
    if (customerFromDB == null) {
      customer = purchase.getCustomer();
    }
    else {
      customer = customerFromDB;
    }
    
    customer.add(order);
    
    // save to the DB
    customerRepository.save(customer);
    
    // return a response
    return new PurchaseResponse(orderTrackingNumber);
  }
  
  private String generateOrderTrackingNumber() {
    
    // generate a random UUID number (we use UUID version4)
    return UUID.randomUUID().toString();
    
  }
}



