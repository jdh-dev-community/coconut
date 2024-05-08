package com.coconut.auth_service.filter;

import com.coconut.auth_service.dto.IdPasswordSignInDto;
import com.coconut.auth_service.service.interfaces.EnhancedDetailService;
import com.coconut.auth_service.wrapper.CustomContentCachingRequestWrapper;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class SignInFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;
  private final EnhancedDetailService userDetailsService;

  public SignInFilter(ObjectMapper objectMapper, EnhancedDetailService userDetailsService) {
    this.objectMapper = objectMapper;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
    CustomContentCachingRequestWrapper cachedRequest = (CustomContentCachingRequestWrapper) request;
    IdPasswordSignInDto dto = cachedRequest.getBody(IdPasswordSignInDto.class);

    try {
      UserDetails userDto = userDetailsService.saveUser(dto);
      onSuccessSignInProcess(response, userDto);
    } catch (Exception e) {
      onFailSignInProcess(request, response, e);
    }


  }

  private void onSuccessSignInProcess(HttpServletResponse response, UserDetails userDetails) throws IOException {
    CustomResponse<UserDetails> result = CustomResponse.of(userDetails);
    String jsonResponse = objectMapper.writeValueAsString(result);

    response.setStatus(HttpServletResponse.SC_CREATED);
    response.setContentType("application/json");
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();
  }

  private void onFailSignInProcess(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
    log.error("fail SignIn: >> ", e);
    HttpErrorInfo error = new HttpErrorInfo(HttpStatus.BAD_REQUEST, request.getRequestURI(), e.getMessage(), e.getClass().getName());
    CustomResponse<HttpErrorInfo> result = CustomResponse.of(error);
    String jsonResponse = objectMapper.writeValueAsString(result);

    response.setStatus(HttpServletResponse.SC_CREATED);
    response.setContentType("application/json; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();
  }

}
