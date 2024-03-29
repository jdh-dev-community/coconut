package com.coconut.quiz_spring.domain.quiz.controller;

import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class QuizController {


  private final QuizService quizService;


  @PostMapping("/quiz")
  public QuizDto generateQuiz() {
    QuizDto result = quizService.generateQuiz();
    return result;
  }

  @PostMapping("/answer")
  public void generateAnswer(@RequestBody Map<String, String> req) throws IOException, InterruptedException {
//    String result = openAiService.generateAnswer(req.toString());
//    return result;
  }


  @GetMapping("/quiz")
  public String testConnect() {
    return "connected: >> ";
  }


}
