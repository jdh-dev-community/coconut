package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.CustomUserDetails;
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

  private final RestTemplate restTemplate;

  private final ObjectMapper objectMapper;

  @Value("${internal_service.user}")
  private String baseUrl;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    String url = new StringBuilder(baseUrl)
            .append("/api/v1")
            .append("/user")
            .append("?email=" + username)
            .toString();

    AuthUserDetails authUser = fetchUser(url);

    UserDetails userDetails = CustomUserDetails.builder()
            .userId(authUser.getUserId())
            .username(authUser.getEmail())
            .password(authUser.getPassword())
            .build();

    return userDetails;
  }

  private AuthUserDetails fetchUser(String url) throws UsernameNotFoundException {
    try {
      ResponseEntity<CustomResponse> responseEntity = restTemplate.getForEntity(url, CustomResponse.class);
      CustomResponse<AuthUserDetails> result = responseEntity.getBody();

      AuthUserDetails details = objectMapper.convertValue(result.getResult(), AuthUserDetails.class);
      return details;
    }
    catch (ResourceAccessException e) {
      throw new RuntimeException("유저 서버에 문제가 발생하였습니다.");
    } catch (Exception e) {
      throw new UsernameNotFoundException("유저 데이터를 가져오는 중 문제가 발생하였습니다. [url: " + url + "]");
    }
  }
}
