package com.coconut.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @PostMapping("/auth/login")
  public String login() {
    return "login";
  }

  @PostMapping("/auth/signin")
  public String signin() {
    return "signin";
  }

  @GetMapping("/auth/health")
  public String check() {
    return "healthy";
  }

}
