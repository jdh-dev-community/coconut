package com.coconut.quiz_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.coconut.global", "com.coconut.jpa_utils", "com.coconut.quiz_service"})
public class QuizApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuizApplication.class, args);
  }

}
