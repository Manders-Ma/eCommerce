package com.manders.ecommerce.filter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsrfCookieFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(CsrfCookieFilter.class);

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

    if (csrfToken != null) {
      try {
        // 同時以 cookie 提供 token 給瀏覽器（非 HttpOnly，讓前端 JS 可讀取，例如 Angular 的 XSRF-TOKEN）
        ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", csrfToken.getToken())
            .httpOnly(false)
            .secure(request.isSecure())
            .path("/")
            .sameSite("Lax")
            .build();

        // addHeader 允許多個 Set-Cookie
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
      } catch (Exception e) {
        // 不阻斷請求流程，只記錄問題
        logger.warn("Failed to write CSRF cookie/header", e);
      }
    }
    filterChain.doFilter(request, response);
  }

}
