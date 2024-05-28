package com.coconut.user_service.domain.user.service;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;
import com.coconut.user_service.domain.user.domain.User;
import com.coconut.user_service.domain.user.repository.UserRepository;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @AfterEach
  public void cleanup() {
    userRepository.deleteAll();
  }

  @Nested
  class 유저_생성_테스트 {
    private UserCreateReqDto userCreateDto;

    @BeforeEach
    public void setup() {
      userCreateDto = UserCreateReqDto.of(
              SignInType.EMAIL,
              "test@gmail.com",
              "test123!",
              "010-1111-1111",
              "test nickname"
      );

    }

    @Test
    public void 유저생성이_성공하면_유저정보를_반영한_UserDto를_반환한다() {
      UserDto userDto = userService.createUser(userCreateDto);

      User user = userRepository.findById(userDto.getUserId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(user.getUserId(), userDto.getUserId());
      assertEquals(userCreateDto.getEmail(), userDto.getEmail());
    }

    @Test
    public void 유저생성시_비밀번호를_암호화한다() {
      String originalPassword = userCreateDto.getPassword();

      UserDto userDto = userService.createUser(userCreateDto);
      User user = userRepository.findById(userDto.getUserId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertTrue(passwordEncoder.matches(originalPassword, user.getPassword()));
    }

    @Test
    public void 이메일_회원가입의_경우_이메일이_없으면_DataIntegrityViolationException예외가_발생() {
      UserCreateReqDto invalidDto = UserCreateReqDto.of(
              SignInType.EMAIL,
              null,
              "test123!",
              "010-1111-1111",
              "test nickname"
      );

      assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(invalidDto));
    }

    @Test
    public void 이메일_회원가입의_경우_비밀번호가_없으면_예외가_발생() {
      UserCreateReqDto invalidDto = UserCreateReqDto.of(
              SignInType.EMAIL,
              null,
              null,
              "010-1111-1111",
              "test nickname"
      );

      assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(invalidDto));
    }
  }

}
