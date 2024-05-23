package com.coconut.auth_service.filter;

import com.coconut.auth_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j

public class JwtValidationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtValidationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("started: >> ");
    String accessToken = extractAccessToken(request);
    log.info("accessToken: >> " + accessToken);

    if (Objects.nonNull(accessToken) && jwtService.validateJWT(accessToken)) {
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("Access Granted");
      response.getWriter().flush();
      return;
    }


    throw new AccessDeniedException("유효하지 않은 요청입니다.");
  }


  private String extractAccessToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    Cookie[] checkedCookie = Objects.isNull(cookies) ? new Cookie[0] : cookies;

    Optional<String> accessToken = Arrays.stream(checkedCookie)
            .peek(cookie -> log.info("Cookie name: " + cookie.getName() + ", value: " + cookie.getValue()))
            .filter(cookie -> "jwt".equals(cookie.getName()))
            .map((cookie -> cookie.getValue()))
            .findFirst();

    return accessToken.orElse(null);

  }
}
