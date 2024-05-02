package com.coconut.auth_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;


@Slf4j
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

  public CustomLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
    super(new AntPathRequestMatcher(defaultFilterProcessesUrl), authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    String username = (String) request.getAttribute("id");
    String password = (String) request.getAttribute("password");

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

    return super.getAuthenticationManager().authenticate(authRequest);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("Authentication success!!");
    SecurityContextHolder.getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    log.info("Authentication fail!!");

    String jsonPayload = String.format("{\"error\": \"아이디 혹은 비밀번호를 확인해주세요.\", \"message\": \"%s\"}", failed.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(jsonPayload);
    response.getWriter().flush();
    response.getWriter().close();

  }
}
