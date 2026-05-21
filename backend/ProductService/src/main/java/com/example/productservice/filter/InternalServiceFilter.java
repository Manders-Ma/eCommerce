package com.example.productservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalServiceFilter extends OncePerRequestFilter {

    static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    @Value("${internal.service.secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String secret = request.getHeader(INTERNAL_SECRET_HEADER);
        if (internalSecret.equals(secret)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "internal-service", null,
                    AuthorityUtils.createAuthorityList("ROLE_INTERNAL"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
