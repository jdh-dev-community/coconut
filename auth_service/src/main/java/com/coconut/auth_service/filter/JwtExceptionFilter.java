package com.coconut.auth_service.filter;

import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper;

  public JwtExceptionFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      handleExpiredJwtException(request, response, e);
    } catch (SignatureException e) {
      handleSignatureException(request, response, e);
    } catch (AccessDeniedException e) {
      log.info("AccessDeniedException: >> " + e.getMessage());
      handleAccessDeniedException(request, response, e);
    }
  }

  private void handleExpiredJwtException(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException e) throws IOException {
    HttpErrorInfo error = new HttpErrorInfo(HttpStatus.BAD_REQUEST, request.getRequestURI(), e.getMessage(), e.getClass().getName());
    CustomResponse<HttpErrorInfo> result = CustomResponse.of(error);
    String jsonResponse = objectMapper.writeValueAsString(result);

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    sendErrorResponse(response, jsonResponse);
  }

  private void handleSignatureException(HttpServletRequest request, HttpServletResponse response, SignatureException e) throws IOException {
    HttpErrorInfo error = new HttpErrorInfo(HttpStatus.UNAUTHORIZED, request.getRequestURI(), e.getMessage(), e.getClass().getName());
    CustomResponse<HttpErrorInfo> result = CustomResponse.of(error);
    String jsonResponse = objectMapper.writeValueAsString(result);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    sendErrorResponse(response, jsonResponse);
  }


  private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    HttpErrorInfo error = new HttpErrorInfo(HttpStatus.UNAUTHORIZED, request.getRequestURI(), e.getMessage(), e.getClass().getName());
    CustomResponse<HttpErrorInfo> result = CustomResponse.of(error);
    String jsonResponse = objectMapper.writeValueAsString(result);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    sendErrorResponse(response, jsonResponse);
  }

  private void sendErrorResponse(HttpServletResponse response, String errorResponse) throws IOException {
    response.setContentType("application/json; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(errorResponse);
    response.getWriter().flush();
  }
}
