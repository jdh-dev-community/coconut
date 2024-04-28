package com.coconut.user_service.domain.user;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

  @Operation(summary = "유저 서비스 헬스 체크", description = "헬스체크")
  @GetMapping("/user/health")
  public boolean checkHealth() {
    return true;
  }
}
