package com.coconut.auth_service.service;

import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.dto.AuthUserDetails;
import com.coconut.global.dto.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServerServiceImpl implements UserServerService {
  private final RestTemplate restTemplate;

  private final ObjectMapper objectMapper;

  @Value("${internal_service.user}")
  private String baseUrl;


  @Override
  public AuthUserDetails findUserByEmail(String email) {
    String url = new StringBuilder(baseUrl + "/api/v1/user")
            .append("?email=" + email)
            .toString();

    try {
      AuthUserDetails authUser = getUserFromUserServer(url);
      return authUser;
    } catch (UsernameNotFoundException e) {
      return null;
    }

  }

  private AuthUserDetails getUserFromUserServer(String url) {
    try {
      ResponseEntity<CustomResponse> responseEntity = restTemplate.getForEntity(url, CustomResponse.class);
      CustomResponse<AuthUserDetails> result = responseEntity.getBody();

      AuthUserDetails details = objectMapper.convertValue(result.getResult(), AuthUserDetails.class);
      return details;
    } catch (NotFound e) {
      log.error("Not Found: >> ", e);
      throw new UsernameNotFoundException("일치하는 유저가 존재하지 않습니다. [url: " + url + "]");
    } catch (ResourceAccessException e) {
      log.error("ResourceAccessException: >> ", e);
      throw new ResourceAccessException("유저 서버에 문제가 발생하였습니다.");
    } catch (Exception e) {
      log.error("Exception: >> ", e);
      throw new RuntimeException("유저 데이터를 가져오는 중 문제가 발생하였습니다. [url: " + url + "]");
    }
  }
}
