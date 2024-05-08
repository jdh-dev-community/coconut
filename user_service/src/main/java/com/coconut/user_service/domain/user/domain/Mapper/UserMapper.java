package com.coconut.user_service.domain.user.domain.Mapper;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;
import com.coconut.user_service.domain.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User from (UserCreateReqDto dto, String EncryptedPassword) {

    return User.builder()
            .email(dto.getEmail())
            .password(EncryptedPassword)
            .nickname(dto.getNickname())
            .mobile(dto.getMobile())
            .signInType(dto.getSigninType())
            .build();
  }

  public UserDto toDto(User user) {
    return UserDto.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .passowrd(user.getPassword())
            .nickname(user.getNickname())
            .mobile(user.getMobile())
            .signinType(SignInType.match(user.getSignInType()))
            .interest(user.getInterest())
            .build();
  }
}
