package com.coconut.user_service.domain.auth.service;

import com.coconut.user_service.domain.auth.service.interfaces.AuthService;
import com.coconut.user_service.domain.auth.strategy.AuthStrategy;
import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final AuthStrategy emailStrategy;

  @Override
  public User signinUser(UserCreateDto dto) {

    // if (dto.getSignInType().equals(Google)) {}

    return emailStrategy.signin(dto);

  }
}
