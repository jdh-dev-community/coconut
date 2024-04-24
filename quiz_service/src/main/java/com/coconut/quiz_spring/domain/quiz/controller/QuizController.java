package com.coconut.quiz_spring.domain.quiz.controller;

import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.common.dto.ListReqDto;
import com.coconut.quiz_spring.domain.quiz.dto.*;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class QuizController {


  private final QuizService quizService;

  @Operation(summary = "채용공고에 연결된 퀴즈 조회", description = "채용공고에 연결된 퀴즈를 조회합니다")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/quiz/jobposting/{id}")
  public CustomResponse<List<QuizDto>> jobPostingQuizList(@PathVariable("id") long id) {
    List<QuizDto> result = quizService.findQuizByJobPostingId(id);
    CustomResponse<List<QuizDto>> response = CustomResponse.of(result);

    return response;
  }

  @Operation(summary = "채점하기", description = "퀴즈 정답 채점 api")
  @PostMapping("/answer")
  public CustomResponse<AnswerDto> generateAnswer(@Valid @RequestBody AnswerCreateReqDto dto) throws Exception {
    AnswerDto answer = quizService.createAnswer(dto);
    CustomResponse<AnswerDto> response = CustomResponse.of(answer);

    return response;
  }





}
