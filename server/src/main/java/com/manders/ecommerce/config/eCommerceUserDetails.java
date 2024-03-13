package com.manders.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.manders.ecommerce.dao.MemberRepository;
import com.manders.ecommerce.entity.Member;


@Service
public class eCommerceUserDetails implements UserDetailsService {
  
  @Autowired
  private MemberRepository memberRepository;

  // 這邊參數叫username，實際上我要用的是email，我的email是Unique Key。
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    String userName = null, password = null;
    List<GrantedAuthority> authorities = null;
    Member member = memberRepository.findByEmail(username);
    
    if (member == null) {
      throw new UsernameNotFoundException("User details not found for the user: " + username);
    }
    
    userName = member.getEmail();
    password = member.getPassword();
    authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(member.getRole()));
    
    return new User(username, password, authorities); 
  }
  
}
