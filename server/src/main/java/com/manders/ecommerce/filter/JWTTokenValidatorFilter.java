package com.manders.ecommerce.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import com.manders.ecommerce.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JWTTokenValidatorFilter.class);


  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
    String jwt = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
    }
    
    if (jwt != null && !jwt.isBlank()) {
      try {
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
        String email = claims.get("email", String.class);
        String authorities = claims.get("authorities", String.class);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, 
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities != null ? authorities : ""));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (ExpiredJwtException e) {
        logger.info("JWT expired: {}", e.getMessage());
        respondWithUnauthorized(response, "JWT expired: " + e.getMessage());
        return;
      } catch (JwtException | IllegalArgumentException e) {
        logger.warn("Invalid JWT: {}", e.getMessage());
        respondWithUnauthorized(response, "Invalid JWT Token");
        return;
      }
    }
    
    filterChain.doFilter(request, response);
  }
  
  private void respondWithUnauthorized(@NonNull HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    String body = String.format("{\"status\":%d,\"error\":\"%s\"}", HttpStatus.UNAUTHORIZED.value(), message);
    response.getWriter().write(body);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {

    for (String url: SecurityConstants.JWT_AUTHENTICATED_URL) {
      if (request.getServletPath().contains(url)) return false;
    }
    return true;
  }
}
