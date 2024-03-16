package com.manders.ecommerce.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import com.manders.ecommerce.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    
    CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    
    http
      /*
       * 當使用者輸入帳號密碼成功後，spring security會將身分驗證資訊存到security context保存，
       * 之後遇到需要存取受保護的API之後，拿存在cookie中的JSESSIONID後端伺服器會從context裡面找對應的session，
       * session會儲存身分驗證的資訊，從裡面提取出來判斷是否有驗證過，讓用戶不必每次都輸入帳號密碼。
       * 
       * SessionCreationPolicy.ALWAYS表示session會不斷更新。
       */
      .securityContext(securityContext -> securityContext.requireExplicitSave(true))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
      
      /* 
       * cors 配置
       */
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
      
      /*
       * csrf 配置
       * spring secrity預設的登入畫面，會讓你輸入帳密之後交給UsernamePasswordAuthenticationFilter
       * 創建UsernamePasswordAuthenticationToken，今天若是在客戶端做登入的動作，就需要前端發送請求出去之前
       * 把用戶的帳密放在HttpHeader的Authoriztion欄位，格式: 'Basic ${username}:${password}'
       * BasicAuthenticationFilter就會啟用並試著包裝成UsernamePasswordAuthenticationToken，
       * 我們才有辦法做後續的驗證動作。
       */
      .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestAttributeHandler)
          .ignoringRequestMatchers("/member/register")
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      )
      .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
      
      /* 
       * spring data rest會解析repository把他們做成restful api，但包含了POST會更改DB數據的API，所以在這邊把他們deny掉。
       * 此外定義哪些API需要通過驗證。
      */
      .authorizeHttpRequests((requests) -> requests
          .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers(HttpMethod.PATCH, "/api/products/**", "/api/product-category/**", "/api/shipping-address/**").denyAll()
          .requestMatchers("/api/customers/**").denyAll()
          .requestMatchers("/api/members/**").denyAll()
          .requestMatchers("/api/profile/**").denyAll()
          .requestMatchers("/api/checkout/purchase", "/member/details").authenticated()
          .anyRequest().permitAll()
      )
      .formLogin(Customizer.withDefaults())
      .httpBasic(Customizer.withDefaults());
    return http.build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }
}
