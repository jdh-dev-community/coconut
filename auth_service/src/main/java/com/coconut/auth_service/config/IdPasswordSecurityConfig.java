package com.coconut.auth_service.config;

import com.coconut.auth_service.filter.*;
import com.coconut.auth_service.service.JwtService;
import com.coconut.auth_service.service.interfaces.EnhancedDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class IdPasswordSecurityConfig {

  private final EnhancedDetailService userDetailsService;

  private final AuthenticationConfiguration authenticationConfiguration;

  private final JwtService jwtService;

  private final ObjectMapper objectMapper;
  private final String loginUri = "/api/v1/auth/login";
  private final String signinUri = "/api/v1/auth/signin";


  @Value("${login_redirect_url}")
  private String redirectUrl;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
    HttpSecurity customizedHttp = setDefaultConfig(http);

    customizedHttp
            .securityMatcher(loginUri)
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(loginUri).authenticated());

    customizedHttp
            .addFilterBefore(new ExceptionHandlerFilter(redirectUrl), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new IdPasswordValidationFilter(objectMapper), ExceptionHandlerFilter.class)
            .addFilterAt(new CustomLoginFilter(loginUri, authenticationConfiguration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JwtFilter(jwtService), CustomLoginFilter.class);

    return http.build();
  }

  @Bean
  public SecurityFilterChain signInFilterChain(HttpSecurity http) throws Exception {
    HttpSecurity customizedHttp = setDefaultConfig(http);

    customizedHttp
            .securityMatcher(signinUri)
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(signinUri).authenticated());

    customizedHttp
            .addFilterBefore(new CachingRequestBodydFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new IdPasswordSigninValidationFilter(), CachingRequestBodydFilter.class)
            .addFilterAfter(new SignInFilter(objectMapper, userDetailsService), IdPasswordSigninValidationFilter.class);

    return customizedHttp.build();

  }

  private HttpSecurity setDefaultConfig(HttpSecurity http) throws Exception {
    return http
            .csrf((auth) -> auth.disable())
            .formLogin((auth) -> auth.disable())
            .httpBasic((auth) -> auth.disable())
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  }

}
