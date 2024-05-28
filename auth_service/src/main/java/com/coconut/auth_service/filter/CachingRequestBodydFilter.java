package com.coconut.auth_service.filter;

import com.coconut.auth_service.wrapper.CustomContentCachingRequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CachingRequestBodydFilter implements Filter {

  private final ObjectMapper objectMapper;

  public CachingRequestBodydFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    CustomContentCachingRequestWrapper cachingRequest = new CustomContentCachingRequestWrapper(httpServletRequest, objectMapper);

    chain.doFilter(cachingRequest, response);
  }
}
