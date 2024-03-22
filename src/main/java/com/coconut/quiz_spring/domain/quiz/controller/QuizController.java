package com.coconut.quiz_spring.domain.quiz.controller;


import com.coconut.quiz_spring.domain.quiz.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class QuizController {

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


}
