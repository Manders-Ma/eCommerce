package com.example.orderservice.repository;

import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

  Customer findByFirstNameAndLastNameAndEmailAndMember(
          String firstName,
          String lastName,
          String email,
          Member member
  );
  
}
