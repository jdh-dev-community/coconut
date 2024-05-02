package com.coconut.user_service.domain.user.service.interfaces;

import com.coconut.global.dto.AuthUserDetails;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import com.coconut.user_service.domain.user.dto.UserDto;

public interface UserService {
  UserDto createUser(UserCreateDto dto);

  AuthUserDetails getUserByEmail(String email);
}
