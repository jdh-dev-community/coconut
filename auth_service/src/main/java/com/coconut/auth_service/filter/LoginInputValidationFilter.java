package com.coconut.auth_service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class LoginInputValidationFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  public LoginInputValidationFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    Map<String, String> loginRequest = objectMapper.readValue(request.getInputStream(), Map.class);

    String id = loginRequest.get("id");
    String password = loginRequest.get("password");

    if (!isValidId(id) || !isValidPassword(password)) {
      responseBadRequest(response);
    }

    request.setAttribute("id", id);
    request.setAttribute("password", password);

    filterChain.doFilter(request, response);
  }

  private boolean isValidId(String id) {
    return Objects.nonNull(id);
  }

  private boolean isValidPassword(String pw) {
    return Objects.nonNull(pw);
  }

  private void responseBadRequest(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}
