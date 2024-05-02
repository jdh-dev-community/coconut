package com.coconut.user_service.domain.user.service;

import com.coconut.global.dto.AuthUserDetails;
import com.coconut.user_service.domain.auth.service.interfaces.AuthService;
import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import com.coconut.user_service.domain.user.dto.UserDto;
import com.coconut.user_service.domain.user.repository.UserRepository;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


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

  @Override
  public AuthUserDetails getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다. [email: " + email + "]"));

    AuthUserDetails detail = AuthUserDetails.of(user.getUserId(), user.getEmail(), user.getPassword());

    return detail;
  }
}
