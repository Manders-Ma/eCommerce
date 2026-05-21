package com.example.orderservice.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.orderservice.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
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

                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            } catch (Exception e) {
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
