package com.manders.ecommerce.config;

import static org.springframework.security.config.Customizer.withDefaults;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      http
      .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
        
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
          config.setAllowedMethods(Collections.singletonList("*"));
          config.setAllowCredentials(true);
          config.setAllowedHeaders(Collections.singletonList("*"));
          config.setMaxAge(3600L);
          return config;
        }
        
      }))
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
      return new BCryptPasswordEncoder();
  }
}
