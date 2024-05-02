package com.coconut.auth_service.filter;

import com.coconut.auth_service.dto.CustomUserDetails;
import com.coconut.auth_service.dto.JwtCreateDto;
import com.coconut.auth_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      CustomUserDetails details = extractUserDetails(securityContext);

      JwtCreateDto dto = JwtCreateDto.of(details.getUserId());
      String jwt = jwtService.generateJWT(dto);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      response.getWriter().write("{\"token\": \"" + jwt + "\"}");
      response.getWriter().flush();
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }

  }

  private CustomUserDetails extractUserDetails(SecurityContext securityContext) {
    Authentication auth = securityContext.getAuthentication();

    if (auth.getPrincipal() instanceof CustomUserDetails) {
      return (CustomUserDetails) auth.getPrincipal();
    } else {
      log.error("잘못된 인증 유저 정보가 확인되었습니다.: " + auth.getPrincipal().getClass());
      throw new RuntimeException("잘못된 인증 유저 정보가 확인되었습니다.");
    }
  }


}
