package com.coconut.user_service.domain.user.controller;

import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.UserDto;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원가입 api", description = "회원가입 api")
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/user")
  public CustomResponse<UserDto> createUser(@Valid @RequestBody UserCreateReqDto dto) {
    UserDto result = userService.createUser(dto);
    return CustomResponse.of(result);
  }

  @Operation(summary = "유저 조회 api", description = "email과 회원가입 타입 기반으로 유저를 조회합니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/user")
  public CustomResponse<UserDto> getUser(@RequestParam("email") String email, @RequestParam("signin_type") SignInType signInType) {
    UserDto result = userService.getUserByEmailAndSignInType(email, signInType);
    return CustomResponse.of(result);
  }


  @Operation(summary = "유저 서비스 헬스 체크", description = "헬스체크")
  @GetMapping("/user/health")
  public boolean checkHealth() {
    return true;
  }
}
