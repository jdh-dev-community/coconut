package com.coconut.user_service.domain.user.service.interfaces;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;

public interface UserService {
  UserDto createUser(UserCreateReqDto dto);
  UserDto getUserByEmailAndSignInType(String email, SignInType signInType);
}
