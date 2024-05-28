package com.coconut.auth_service.service;

import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServerServiceImpl implements UserServerService {
  private final RestTemplate restTemplate;

  private final ObjectMapper objectMapper;

  @Value("${internal_service.user}")
  private String baseUrl;


  @Override
  public UserDto upsertUser(UserCreateReqDto details) {
    String email = details.getEmail();
    SignInType signInType = details.getSigninType();

    UserDto existedUser = findUserByEmailAndSignInType(email, signInType);

    if (Objects.isNull(existedUser)) {
      return saveUser(details);
    } else {
      // 유저 정보 업데이트
      return existedUser;
    }

  }

  @Override
  public UserDto saveUser(UserCreateReqDto details) {
    String url = baseUrl + "/api/v1/user";
    UserDto user = saveUserInUserServer(url, details);

    return user;
  }

  @Override
  public UserDto findUserByEmailAndSignInType(String email, SignInType signInType) {
    String url = new StringBuilder(baseUrl + "/api/v1/user")
            .append("?email=" + email)
            .append("&signin_type=" + signInType.getType())
            .toString();

    try {
      UserDto authUser = getUserFromUserServer(url);
      return authUser;
    } catch (UsernameNotFoundException e) {
      return null;
    }
  }

  private UserDto getUserFromUserServer(String url) {
    try {
      ResponseEntity<CustomResponse> responseEntity = restTemplate.getForEntity(url, CustomResponse.class);
      CustomResponse<UserDto> result = responseEntity.getBody();

      UserDto details = objectMapper.convertValue(result.getResult(), UserDto.class);
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

  private UserDto saveUserInUserServer(String url, Object body) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Object> request = new HttpEntity<>(body, headers);

      ResponseEntity<CustomResponse> response = restTemplate.postForEntity(url, request, CustomResponse.class);
      CustomResponse<UserDto> result = response.getBody();

      UserDto details = objectMapper.convertValue(result.getResult(), UserDto.class);

      return details;
    } catch (BadRequest e) {
      throw e;
    } catch (ResourceAccessException e) {
      log.error("ResourceAccessException: >> ", e);
      throw new ResourceAccessException("유저 서버에 문제가 발생하였습니다.");
    } catch (Exception e) {
      log.error("Exception: >> ", e);
      throw new RuntimeException("유저 데이터를 저장하는 중 문제가 발생하였습니다. [url: " + url + "]");
    }

  }
}
