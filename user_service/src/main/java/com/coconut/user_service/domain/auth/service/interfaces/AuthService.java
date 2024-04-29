package com.coconut.user_service.domain.auth.service.interfaces;

import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;

public interface AuthService {
  User signinUser(UserCreateDto dto);
}
