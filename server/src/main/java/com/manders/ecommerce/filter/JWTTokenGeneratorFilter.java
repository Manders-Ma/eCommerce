package com.manders.ecommerce.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.manders.ecommerce.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    
    /* 
     * SecurityContext : 一個驗證過的Authentication 物件。 
     * SecurityContextHolder : 用來儲存驗證過的Authentication ，也就是負責管理SecurityContext。
    */
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
      Date start = new Date();
      Date end = new Date(start.getTime() + SecurityConstants.JWT_EXPIRATION_TIME);
      String jwt = Jwts.builder()
          .issuer("ecommerce").subject("JWT Token")
          .claim("email", authentication.getName())
          .claim("authorities", populateAuthorities(authentication.getAuthorities()))
          .issuedAt(start)
          .expiration(end)
          .signWith(key).compact();
      
      response.setHeader(SecurityConstants.JWT_HEADER, jwt);
    }
    
    filterChain.doFilter(request, response);
    
  }
  
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getServletPath().equals("/member/details");
  }
  
  private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
    Set<String> authoritiesSet = new HashSet<>();
    for (GrantedAuthority authority: collection) {
      authoritiesSet.add(authority.getAuthority());
    }
    
    return String.join(",", authoritiesSet);
  }

}
