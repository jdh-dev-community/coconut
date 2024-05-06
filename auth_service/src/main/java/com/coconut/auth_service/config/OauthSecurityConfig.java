package com.coconut.auth_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OauthSecurityConfig {
  private final String oauthLogin = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/*";
  private final String oauthRedirect = OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

  @Autowired
  private Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

  @Bean
  public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {

    return http
            .securityMatcher(oauthLogin, oauthRedirect)
            .csrf((auth) -> auth.disable())
            .authorizeHttpRequests(auth -> {
              auth.anyRequest().authenticated();
            }).oauth2Login((auth) -> {
              auth.successHandler(oauth2LoginSuccessHandler);
            }).build();

  }

}
