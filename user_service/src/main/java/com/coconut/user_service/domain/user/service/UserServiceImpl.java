package com.coconut.user_service.domain.user.service;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;
import com.coconut.user_service.domain.user.domain.Mapper.UserMapper;
import com.coconut.user_service.domain.user.domain.User;

import com.coconut.user_service.domain.user.repository.UserRepository;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  private final UserRepository userRepository;

  @Override
  public UserDto createUser(UserCreateReqDto dto) {
    String encryptedPassword = passwordEncoder.encode(dto.getPassword());
    User user = userMapper.from(dto, encryptedPassword);

    userRepository.save(user);

    return userMapper.toDto(user);
  }

  @Override
  public UserDto getUserByEmailAndSignInType(String email, SignInType signInType) {
    User user = userRepository.findByEmailAndSignInType(email, signInType.getType())
            .orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다. [email: " + email + "]"));

    return userMapper.toDto(user);
  }
}
