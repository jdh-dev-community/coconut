package com.jdh.community_spring.common.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@WebFilter(filterName = "LogFilter", urlPatterns = {"/api/v1/*"})
public class LogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    long startTime = System.currentTimeMillis();
    log.info("[Filter] Request In: {} {}", request.getMethod(), request.getRequestURI());

    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("[Filter] Request: {} {} | Error: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
      throw e; // Re-throw the exception for further handling if necessary
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("[Filter] Request Out: {} {} | Status: {} | End Time: {} | Duration: {} ms",
              request.getMethod(), request.getRequestURI(), response.getStatus(), endTime, (endTime - startTime));

      if (!response.isCommitted()) {
        log.warn("[Filter] Request: {} {} | Warning: Response not committed", request.getMethod(), request.getRequestURI());
      }
    }
  }
}
