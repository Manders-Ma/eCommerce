package com.example.productservice.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewayInternalJwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(GatewayInternalJwtFilter.class);

    public static final String INTERNAL_AUTH_HEADER = "X-Gateway-Auth";
    private static final String EXPECTED_ISSUER = "gateway";

    @Value("${gateway.internal.jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(INTERNAL_AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7).trim();
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .requireIssuer(EXPECTED_ISSUER)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            var authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject() != null ? claims.getSubject() : "internal-service",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_INTERNAL"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            logger.info("Gateway internal JWT expired: {}", e.getMessage());
            respondWithUnauthorized(response, "Gateway internal JWT expired");
            return;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid gateway internal JWT: {}", e.getMessage());
            respondWithUnauthorized(response, "Invalid gateway internal JWT");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void respondWithUnauthorized(@NonNull HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String body = String.format("{\"status\":%d,\"error\":\"%s\"}", HttpStatus.UNAUTHORIZED.value(), message);
        response.getWriter().write(body);
    }
}
