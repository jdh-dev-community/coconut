package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.CustomUserDetails;
import com.coconut.auth_service.dto.IdPasswordSignInDto;
import com.coconut.auth_service.service.interfaces.EnhancedDetailService;
import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class IdPasswordUserDetailService implements EnhancedDetailService {

  private final UserServerService userServerService;

  @Override
  public UserDetails loadUserByEmailAndSignInType(String email, SignInType signInType) {
    UserDto userDto = userServerService.findUserByEmailAndSignInType(email, signInType);
    if (Objects.isNull(userDto)) return null;

    UserDetails userDetails = CustomUserDetails.builder()
            .signInType(userDto.getSigninType())
            .userId(userDto.getUserId())
            .username(userDto.getEmail())
            .password(userDto.getPassowrd())
            .nickname(userDto.getNickname())
            .build();

    return userDetails;

  }

  @Override
  public UserDetails saveUser(IdPasswordSignInDto dto) {
    UserDetails existedUser = loadUserByEmailAndSignInType(dto.getEmail(), dto.getSignInType());

    if (Objects.isNull(existedUser)) {
      UserCreateReqDto signInDto = createSignInUserDetail(dto);
      UserDto userDto = userServerService.saveUser(signInDto);

      UserDetails userDetails = CustomUserDetails.builder()
              .signInType(userDto.getSigninType())
              .userId(userDto.getUserId())
              .username(userDto.getEmail())
              .nickname(userDto.getNickname())
              .build();

      return userDetails;
    } else {
      throw new RuntimeException("중복된 이메일입니다.");
    }
  }

  private UserCreateReqDto createSignInUserDetail(IdPasswordSignInDto dto) {
    SignInType signInType = dto.getSignInType();
    String email = dto.getEmail();
    String password = dto.getPassword();
    String mobile = dto.getMobile();
    String nickname = UUID.randomUUID().toString();

    return UserCreateReqDto.of(signInType, email, password, mobile, nickname);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return loadUserByEmailAndSignInType(email, SignInType.EMAIL);
  }
}
