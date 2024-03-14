package com.coconut.quiz_spring.domain.quiz.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {

  @GetMapping("/")
  public String getQuiz() {
    return "hi222";
  }
}
