package com.coconut.auth_service.service.interfaces;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;

public interface UserServerService {
  UserDto saveUser(UserCreateReqDto details);
  UserDto findUserByEmailAndSignInType(String email, SignInType signInType);

  UserDto upsertUser(UserCreateReqDto details);
}
