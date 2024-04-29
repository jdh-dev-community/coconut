package com.coconut.user_service.domain.auth.strategy;

import com.coconut.user_service.domain.user.domain.Mapper.UserMapper;
import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailStrategyImpl implements AuthStrategy {

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Override
  public User signin(UserCreateDto dto) {
    if (Objects.isNull(dto.getEmail()) || Objects.isNull(dto.getPassword())) {
      throw new IllegalArgumentException("이메일 회원가입시에 이메일과 비밀번호는 필수값입니다.");
    }

    String encryptedPassword = passwordEncoder.encode(dto.getPassword());
    return userMapper.from(dto, encryptedPassword);
  }
}
