package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
  Customer findByFirstNameAndLastNameAndEmailAndMember(
          String firstName,
          String lastName,
          String email,
          Member member
  );
  
}
