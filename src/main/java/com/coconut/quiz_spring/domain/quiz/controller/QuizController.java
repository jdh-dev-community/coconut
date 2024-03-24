package com.coconut.quiz_spring.domain.quiz.controller;


import com.coconut.quiz_spring.domain.quiz.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class QuizController {

  @Value("${custom_key.test}")
  private String logTest;

  @Autowired
  private OpenAiService openAiService;


  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/quiz")
  public String generateQuiz() throws IOException, InterruptedException {
    String result = openAiService.generateQuiz();
    return result;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/answer")
  public String generateAnswer(@RequestBody Map<String, String> req) throws IOException, InterruptedException {

    String result = openAiService.generateAnswer(req.toString());

    log.info("result: >>" + result);
    return result;
  }


  @GetMapping("/quiz")
  public String testConnect() {
    return "connected: >> " + logTest;
  }


}
