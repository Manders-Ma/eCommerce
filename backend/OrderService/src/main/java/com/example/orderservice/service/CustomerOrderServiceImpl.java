package com.example.orderservice.service;

import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Member;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Customer customerFromDB = this.customerRepository.findByFirstNameAndLastNameAndEmailAndMember(
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
