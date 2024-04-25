package com.jdh.community_spring.common.filter;

import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@WebFilter(filterName = "tokenFilter", urlPatterns = {"/api/v1/post/*"})
public class TokenFilter implements Filter {

  private final InMemoryDBProvider inMemoryDBProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String method = httpRequest.getMethod();


    switch (method.toUpperCase()) {
      case "DELETE":
      case "PUT":
        if (!isValidToken(httpRequest, httpResponse)) {
          httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 토큰입니다.");
          return;
        }
      default:
        log.info("[Filter] Pass auth filter: {}" , httpRequest.getRequestURI());
        chain.doFilter(request, response);
    }
  }

  private boolean isValidToken(HttpServletRequest request, HttpServletResponse httpResponse) {
    String enteredToken = extractToken(request);
    String pathVariable = extractPathVariable(request);

    String token = inMemoryDBProvider.get(pathVariable);

    return enteredToken.equals(token);
  }

  private String extractPathVariable(HttpServletRequest request) {
    String[] parts = request.getRequestURI().split("/");
    return parts[parts.length - 1];
  }

  private String extractToken(HttpServletRequest request) {
    String PREFIX_KEY = "Bearer ";
    String authHeader = request.getHeader("Authorization");
    boolean hasToken = authHeader != null && authHeader.contains(PREFIX_KEY);

    return hasToken ? authHeader.substring(PREFIX_KEY.length()) : "";

  }
}
