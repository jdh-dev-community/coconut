package com.coconut.user_service.domain.user.controller;


import com.coconut.global.dto.CustomResponse;
import com.coconut.user_service.domain.user.dto.UserCreateDto;
import com.coconut.user_service.domain.user.dto.UserDto;
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
  @PostMapping("/user/signin")
  public CustomResponse<UserDto> signin(@Valid @RequestBody UserCreateDto dto) {
    UserDto result = userService.createUser(dto);
    return CustomResponse.of(result);
  }

  @Operation(summary = "유저 서비스 헬스 체크", description = "헬스체크")
  @GetMapping("/user/health")
  public boolean checkHealth() {
    return true;
  }
}
