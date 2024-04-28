package com.coconut.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.coconut.global", "com.coconut.jpa_utils", "com.coconut.user_service"})
public class UserApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class);
  }
}