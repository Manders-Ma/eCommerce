package com.manders.ecommerce.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      http
      .csrf().disable()
      .authorizeHttpRequests((requests) -> requests
          .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.PATCH, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers("/api/customers/**").denyAll()
          .requestMatchers("/api/members/**").denyAll()
          .requestMatchers("/api/profile/**").denyAll()
          .requestMatchers("/api/checkout/purchase").authenticated()
          .anyRequest().permitAll()
      )
      .formLogin(withDefaults())
      .httpBasic(withDefaults());
      return http.build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
      return NoOpPasswordEncoder.getInstance();
  }
}
