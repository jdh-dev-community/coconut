package com.coconut.auth_service.filter;

import com.coconut.auth_service.dto.CustomOauth2User;
import com.coconut.auth_service.dto.JwtCreateDto;
import com.coconut.auth_service.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final JwtService jwtService;

  @Value("${login_redirect_url}")
  private String redirectUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws ServletException, IOException {
    log.info("Authentication success!!");
    CustomOauth2User userDetails = (CustomOauth2User) authentication.getPrincipal();

    JwtCreateDto dto = JwtCreateDto.of(userDetails.getUserId());
    ResponseCookie jwtCookie = jwtService.generateJwtInCookie(dto);
    response.addHeader("Set-Cookie", jwtCookie.toString());

    this.setDefaultTargetUrl(redirectUrl);
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
