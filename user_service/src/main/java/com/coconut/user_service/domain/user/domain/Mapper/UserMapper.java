package com.coconut.user_service.domain.user.domain.Mapper;

import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User from (UserCreateDto dto, String EncryptedPassword) {
    return User.builder()
            .email(dto.getEmail())
            .password(EncryptedPassword)
            .nickname(dto.getNickname())
            .mobile(dto.getMobile())
            .signinType(dto.getSigninType().getType())
            .interest(dto.getInterest())
            .build();
  }
}
