package com.coconut.auth_service.config;

import com.coconut.auth_service.filter.ExceptionHandlerFilter;
import com.coconut.auth_service.filter.LoginInputValidationFilter;
import com.coconut.auth_service.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OauthSecurityConfig {

  private final CustomOauth2UserService customOauth2UserService;
  private final String oauthLogin = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/*";
  private final String oauthRedirect = OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

  @Value("${login_redirect_url}")
  private String redirectUrl;

  @Autowired
  private Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

  @Bean
  public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {

    http
            .securityMatcher(oauthLogin, oauthRedirect)
            .csrf((auth) -> auth.disable())
            .authorizeHttpRequests(auth -> {
              auth.anyRequest().authenticated();
            }).oauth2Login((auth) -> {
              auth
                      .userInfoEndpoint(config -> config.userService(customOauth2UserService))
                      .successHandler(oauth2LoginSuccessHandler);
            });

    http
            .addFilterBefore(new ExceptionHandlerFilter(redirectUrl), OAuth2LoginAuthenticationFilter.class);

    return http.build();
  }

}
