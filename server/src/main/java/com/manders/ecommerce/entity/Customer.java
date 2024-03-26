package com.manders.ecommerce.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "last_name")
  private String lastName;
  
  @Column(name = "email")
  private String email;
  
  @Column(name = "date_created")
  @CreationTimestamp
  private Date dateCreated;
  
  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private Set<Order> orders = new HashSet<>();
  
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;
  
  public void add(Order order) {
    if (order != null) {
      if (orders == null) {
        orders = new HashSet<>();
      }
      
      orders.add(order);
      order.setCustomer(this);
    }
  }
}



