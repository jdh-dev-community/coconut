package com.coconut.auth_service.config;

import com.coconut.auth_service.constant.ProtectedPath;
import com.coconut.auth_service.filter.JwtExceptionFilter;
import com.coconut.auth_service.filter.JwtValidationFilter;
import com.coconut.auth_service.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TokenSecurityConfig {

  private final ProtectedPath protectedPath;

  private final JwtService jwtService;

  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
    http
            .securityMatcher(protectedPath.getRoute())
            .csrf((auth) -> auth.disable())
            .formLogin((auth) -> auth.disable())
            .httpBasic((auth) -> auth.disable())
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http
            .authorizeHttpRequests((auth) -> auth
                    .anyRequest().authenticated());

    http
            .addFilterBefore(new JwtExceptionFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new JwtValidationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

    return http.build();

  }

}
