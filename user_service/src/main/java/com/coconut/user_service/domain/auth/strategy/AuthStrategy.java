package com.coconut.user_service.domain.auth.strategy;

import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;

public interface AuthStrategy {
  User signin(UserCreateDto dto);
}
