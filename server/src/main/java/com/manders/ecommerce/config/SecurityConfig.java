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
import com.manders.ecommerce.filter.JWTTokenGeneratorFilter;
import com.manders.ecommerce.filter.JWTTokenValidatorFilter;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    
    CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    
    // 幫助設定API的存取權限，定義哪些API不可存取或是只能給管理員存取等等。
    String[] needAuthApis = new String[] {"/checkout/purchase", "/member/details", "/pay/**"};
    String[] modifyDBApis = new String[] {"/api/products/**", "/api/product-category/**", "/api/shipping-address/**"};
    String[] needRoleAdminApis = new String[] {"/inventory"};
    
    http
      /*
       * 因為使用JWT進行驗證以及授權所以修改sessionCreationPolicy。
       * 改成SessionCreationPolicy.STATELESS，表示不要生成JSESSIONID，因為jwt token本身就含有使用者詳細資料，
       * 不需要再用http session來儲存。
       */
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      
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
          // 要讓client端知道有這個header，所以做下面的設定，spring security處理了csrf token的header
          config.setExposedHeaders(Arrays.asList(new String[] {"Authorization"}));
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
      .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
      
      /*
       * api 權限設定
       */
      .authorizeHttpRequests((requests) -> requests
          .requestMatchers(HttpMethod.POST, modifyDBApis).hasRole("ADMIN")
          .requestMatchers(HttpMethod.PUT, modifyDBApis).hasRole("ADMIN")
          .requestMatchers(HttpMethod.DELETE, modifyDBApis).hasRole("ADMIN")
          .requestMatchers(HttpMethod.PATCH, modifyDBApis).hasRole("ADMIN")
          .requestMatchers(needRoleAdminApis).hasRole("ADMIN")
          .requestMatchers(needAuthApis).authenticated()
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
