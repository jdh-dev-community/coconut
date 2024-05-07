package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.CustomUserDetails;
import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.dto.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserServerService userServerService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    AuthUserDetails authUser = userServerService.findUserByEmail(email);

    UserDetails userDetails = CustomUserDetails.builder()
            .userId(authUser.getUserId())
            .username(authUser.getEmail())
            .password(authUser.getPassword())
            .build();

    return userDetails;
  }

}
