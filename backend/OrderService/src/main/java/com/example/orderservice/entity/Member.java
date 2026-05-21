package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Data
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long memberId;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "email")
  private String email;
  
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "password")
  private String password;
  
  @Column(name = "role")
  private String role;
  
  @Column(name = "date_created")
  @CreationTimestamp
  private Date dateCreated;
  
  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
  private Set<Customer> customers = new HashSet<>();
  
  public void add(Customer customer) {
    if (this.customers == null) {
      this.customers = new HashSet<>();
    }
    this.customers.add(customer);
    customer.setMember(this);
  }
}



