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


  @Operation(summary = "퀴즈생성", description = "외부에서 생성된 퀴즈를 추가합니다.")
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/quiz/external")
  public CustomResponse<QuizDto> insertExternalQuiz(@Valid @RequestBody QuizDto dto) {
    QuizDto result = quizService.insertExternalQuiz(dto);
    CustomResponse<QuizDto> response = CustomResponse.of(result);

    return response;
  }

  @Operation(summary = "퀴즈생성", description = "퀴즈 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/quizzes")
  public CustomResponse<List<QuizDto>> getQuizList(@Valid @ModelAttribute ListReqDto listReqDto) {
    List<QuizDto> result = quizService.getQuizList(listReqDto);
    CustomResponse<List<QuizDto>> response = CustomResponse.of(result);

    return response;


  }

}
