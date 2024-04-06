package com.coconut.quiz_spring.domain.quiz.controller;

import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerCreateReqDto;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizToJobPostingDto;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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

  @Operation(summary = "채용공고 연결", description = "퀴즈와 채용공고를 연결합니다.")
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/quiz/jobposting")
  public CustomResponse<List<QuizDto>> mapQuizToJobPosting(@RequestBody QuizToJobPostingDto dto) {
    List<QuizDto> result = quizService.mapQuizToJobPosting(dto);
    CustomResponse<List<QuizDto>> response = CustomResponse.of(result);

    return response;

  }

  @Operation(summary = "채용공고에 연결된 퀴즈 조회", description = "채용공고에 연결된 퀴즈를 조회합니다")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/quiz/jobposting/{id}")
  public CustomResponse<List<QuizDto>> jobPostingQuizList(@PathVariable("id") long id) {
    List<QuizDto> result = quizService.findQuizByJobPostingId(id);
    CustomResponse<List<QuizDto>> response = CustomResponse.of(result);

    return response;
  }

  @PostMapping("/answer")
  public CustomResponse<AnswerDto> generateAnswer(@Valid @RequestBody AnswerCreateReqDto dto) throws Exception {
    AnswerDto answer = quizService.createAnswer(dto);
    CustomResponse<AnswerDto> response = CustomResponse.of(answer);

    return response;
  }


  @GetMapping("/quiz")
  public String testConnect() {
    return "connected: >> ";
  }


}
