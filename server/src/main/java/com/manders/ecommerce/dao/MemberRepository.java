package com.manders.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.manders.ecommerce.entity.Member;

@RepositoryRestResource(exported = false)
public interface MemberRepository extends JpaRepository<Member, Long>{
  
  @Query(value = "select m from Member m where m.email=:email")
  Member findByEmail(@Param("email") String email);
}
