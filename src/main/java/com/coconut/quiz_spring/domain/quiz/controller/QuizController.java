package com.coconut.quiz_spring.domain.quiz.controller;

import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class QuizController {


  private final QuizService quizService;


  @Operation(summary = "퀴즈생성", description = "퀴즈를 생성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/quiz")
  public CustomResponse<QuizDto> generateQuiz() {
    QuizDto result = quizService.generateQuiz();
    CustomResponse<QuizDto> response = CustomResponse.of(result);

    return response;
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
