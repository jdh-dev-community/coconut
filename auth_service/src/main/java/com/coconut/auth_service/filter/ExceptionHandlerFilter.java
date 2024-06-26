package com.coconut.auth_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  private final String redirectUrl;

  public ExceptionHandlerFilter(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (ResourceAccessException e) {
      handleResourceAccessException(e, response);
    } catch (Conflict e) {
      handleConflictException(e, response);
    }
  }

  private void handleResourceAccessException(ResourceAccessException e, HttpServletResponse response) throws IOException {
    log.info("handleResourceAccessException: >> redirect to " + redirectUrl);
    String url = redirectUrl + "?reason=" + URLEncoder.encode(e.getMessage(), "UTF-8");
    response.sendRedirect(url);
  }

  private void handleConflictException(Conflict e, HttpServletResponse response) throws IOException {
    log.info("handleConflictException: >> redirect to " + redirectUrl);
    String url = redirectUrl + "?reason=" + URLEncoder.encode("동일한 email의 회원이 존재합니다.", "UTF-8");

    response.setCharacterEncoding("UTF-8");
    response.sendRedirect(url);
  }
}
