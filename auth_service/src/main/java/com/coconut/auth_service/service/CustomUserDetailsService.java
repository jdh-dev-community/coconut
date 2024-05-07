package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.CustomUserDetails;
import com.coconut.auth_service.service.interfaces.AuthUserService;
import com.coconut.global.dto.AuthUserDetails;
import com.coconut.global.dto.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthUserService authUserService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    AuthUserDetails authUser = authUserService.getUserFromUseServer(username);

    UserDetails userDetails = CustomUserDetails.builder()
            .userId(authUser.getUserId())
            .username(authUser.getEmail())
            .password(authUser.getPassword())
            .build();

    return userDetails;
  }

}
