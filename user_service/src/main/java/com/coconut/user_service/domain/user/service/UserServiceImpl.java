package com.coconut.user_service.domain.user.service;

import com.coconut.user_service.domain.auth.service.interfaces.AuthService;
import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import com.coconut.user_service.domain.user.dto.UserDto;
import com.coconut.user_service.domain.user.repository.UserRepository;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final AuthService authService;

  private final UserRepository userRepository;

  @Override
  public UserDto createUser(UserCreateDto dto) {
    User user = authService.signinUser(dto);

    userRepository.save(user);
    return UserDto.from(user);
  }
}
