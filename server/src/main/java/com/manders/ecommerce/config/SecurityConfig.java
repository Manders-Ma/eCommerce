package com.manders.ecommerce.config;

import java.util.List;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.manders.ecommerce.filter.CsrfCookieFilter;
import com.manders.ecommerce.filter.JWTTokenGeneratorFilter;
import com.manders.ecommerce.filter.JWTTokenValidatorFilter;

@Configuration
public class SecurityConfig {

  private static final String FRONTEND_ORIGIN = "http://localhost:4200";

  private static final String[] AUTHENTICATED_APIS = {
      "/checkout/purchase",
      "/member/details",
      "/pay/**"
  };

  private static final String[] ADMIN_WRITE_APIS = {
      "/products/**",
      "/product-category/**",
      "/shipping-address/**"
  };

  private static final String[] ADMIN_APIS = {
      "/inventory"
  };

  private static final String[] CSRF_IGNORED_APIS = {
      "/member/register",
  };

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers(CSRF_IGNORED_APIS))
        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(HttpMethod.POST, ADMIN_WRITE_APIS).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, ADMIN_WRITE_APIS).hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, ADMIN_WRITE_APIS).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, ADMIN_WRITE_APIS).hasRole("ADMIN")
            .requestMatchers(ADMIN_APIS).hasRole("ADMIN")
            .requestMatchers(AUTHENTICATED_APIS).authenticated()
            .anyRequest().permitAll())
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(FRONTEND_ORIGIN));
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization", "X-XSRF-TOKEN"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
