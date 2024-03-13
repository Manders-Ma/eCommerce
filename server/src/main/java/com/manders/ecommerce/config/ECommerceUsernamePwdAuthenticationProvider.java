package com.manders.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.manders.ecommerce.dao.MemberRepository;
import com.manders.ecommerce.entity.Member;

@Component
public class ECommerceUsernamePwdAuthenticationProvider implements AuthenticationProvider {
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getName();
    String pwd = authentication.getCredentials().toString();
    Member member = memberRepository.findByEmail(email);
    
    if (member != null) {
      
      if (passwordEncoder.matches(pwd, member.getPassword())) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        return new UsernamePasswordAuthenticationToken(email, pwd, authorities);
      }
      else {
        throw new BadCredentialsException("密碼無效!");
      }
      
    }
    else {
      throw new BadCredentialsException("輸入的帳號不存在!");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    // 和DaoAuthenticationProvider的supports一樣
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
  
}
