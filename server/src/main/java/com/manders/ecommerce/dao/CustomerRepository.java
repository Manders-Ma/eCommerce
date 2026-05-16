package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
  
  @Query(value = "select c from Customer c where c.firstName=:firstName and c.lastName=:lastName and c.email=:email and c.member=:member")
  Customer findCustomer(
      @Param("firstName") String firstName, 
      @Param("lastName") String lastName, 
      @Param("email") String email, 
      @Param("member") Member member
  );
  
}
