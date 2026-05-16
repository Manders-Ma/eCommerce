package com.manders.ecommerce.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import com.manders.ecommerce.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JWTTokenGeneratorFilter.class);

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    /*
     * SecurityContext : 一個驗證過的Authentication 物件。
     * SecurityContextHolder : 用來儲存驗證過的Authentication ，也就是負責管理SecurityContext。
     */
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      try {
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiresAt = Date.from(now.plusMillis(SecurityConstants.JWT_EXPIRATION_TIME));

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .distinct()
            .collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .issuer("ecommerce")
                .subject(authentication.getName())
                .claim("email", authentication.getName())
                .claim("authorities", authorities)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
            .signWith(key)
            .compact();

        // 使用標準 Authorization header 並帶上 Bearer 前綴
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
      } catch (Exception e) {
        // 生成 token 失敗不阻斷流程，但記錄日誌以便排查
        logger.warn("Failed to generate JWT token", e);
      }
    }

    filterChain.doFilter(request, response);

  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    return !request.getServletPath().equals("/member/details");
  }

}
