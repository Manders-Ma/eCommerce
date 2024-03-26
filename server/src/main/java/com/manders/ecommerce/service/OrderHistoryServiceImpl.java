package com.manders.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manders.ecommerce.dao.MemberRepository;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;
import com.manders.ecommerce.entity.Order;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
  
  @Autowired
  private MemberRepository memberRepository;

  @Override
  @Transactional
  public List<Order> getOrderHistory(String email) {
    
    Member member = memberRepository.findByEmail(email);
    List<Order> orderHistories = new ArrayList<>();
    
    for (Customer customer: member.getCustomers()) {
      customer.getOrders().forEach(order -> orderHistories.add(order));
    }
    
    return orderHistories;
  }

}
