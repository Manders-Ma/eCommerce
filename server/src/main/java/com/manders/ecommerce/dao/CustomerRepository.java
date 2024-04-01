package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.manders.ecommerce.entity.Customer;
import com.manders.ecommerce.entity.Member;


/* 
 * 在實作save order to DB功能中，只有customer repository的原因是在定義Customer entity時，
 * 因為customer跟order是一對多關係，有加入一個order property，我們可以將這個order property包裝完
 * (設定好shippingAddress﹑orderitems及生成order tracking number)之後save customer實例，
 * 即可完成save order to DB對資料庫的所有操作。
*/
@RepositoryRestResource(exported = false)
public interface CustomerRepository extends JpaRepository<Customer, Long>{
  
  @Query(value = "select c from Customer c where c.firstName=:firstName and c.lastName=:lastName and c.email=:email and c.member=:member")
  Customer findCustomer(
      @Param("firstName") String firstName, 
      @Param("lastName") String lastName, 
      @Param("email") String email, 
      @Param("member") Member member
  );
  
}
