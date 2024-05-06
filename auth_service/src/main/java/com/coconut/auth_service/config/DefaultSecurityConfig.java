package com.coconut.auth_service.config;

import com.coconut.auth_service.filter.CustomLoginFilter;
import com.coconut.auth_service.filter.JwtFilter;
import com.coconut.auth_service.filter.LoginInputValidationFilter;
import com.coconut.auth_service.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;

  private final JwtService jwtService;

  private final ObjectMapper objectMapper;
  private final String loginUri = "/api/v1/auth/login";


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {

    http
            .securityMatcher(loginUri)
            .csrf((auth) -> auth.disable())
            .formLogin((auth) -> auth.disable())
            .httpBasic((auth) -> auth.disable())
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(loginUri).authenticated());

    http
            .addFilterBefore(new LoginInputValidationFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new CustomLoginFilter(loginUri, authenticationConfiguration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


}
