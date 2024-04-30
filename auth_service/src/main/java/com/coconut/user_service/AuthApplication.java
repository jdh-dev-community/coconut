package com.coconut.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.coconut.global", "com.coconut.auth_service"})
public class AuthApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class);
  }
}