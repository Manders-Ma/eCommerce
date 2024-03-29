package com.manders.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.dao.CustomerRepository;
import com.manders.ecommerce.dao.MemberRepository;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;
import com.manders.ecommerce.entity.Order;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public void saveOrder(Customer customer, Member member, Order order) {
    // retrieve the member from dto
    Member memberFromDB = this.memberRepository.findByEmail(member.getEmail());
    
    // populate customer with order
    Customer customerFromDB = this.customerRepository.findCustomer(
        customer.getFirstName(), 
        customer.getLastName(), 
        customer.getEmail(), 
        memberFromDB
    );
    
    if (customerFromDB != null) {
      customer = customerFromDB;
    }
    
    customer.add(order);
    
    // populate customer with member
    customer.setMember(memberFromDB);
    memberFromDB.getCustomers().add(customer);
    
    // save to the DB
    memberRepository.save(memberFromDB);
  }

}
