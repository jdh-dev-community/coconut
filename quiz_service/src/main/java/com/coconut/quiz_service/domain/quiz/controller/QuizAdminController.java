package com.coconut.quiz_service.domain.quiz.controller;

import com.coconut.global.dto.CustomResponse;
import com.coconut.jpa_utils.dto.ListReqDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizToJobPostingDto;
import com.coconut.quiz_service.domain.quiz.service.interfaces.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class QuizAdminController {

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

  @Operation(summary = "외부 퀴즈 생성", description = "외부에서 생성된 퀴즈를 추가합니다.")
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/quiz/external")
  public CustomResponse<QuizDto> insertExternalQuiz(@Valid @RequestBody QuizDto dto) {
    QuizDto result = quizService.insertExternalQuiz(dto);
    CustomResponse<QuizDto> response = CustomResponse.of(result);

    return response;
  }


  @Operation(summary = "목록 조회", description = "퀴즈 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/quizzes")
  public CustomResponse<List<QuizDto>> getQuizList(@Valid @ModelAttribute ListReqDto listReqDto) {
    List<QuizDto> result = quizService.getQuizList(listReqDto);
    CustomResponse<List<QuizDto>> response = CustomResponse.of(result);

    return response;
  }


  @Operation(summary = "모든 퀴즈 삭제", description = "[주의] 모든 퀴즈를 삭제합니다.")
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/quizzes")
  public void deleteAllQuiz() {
    quizService.deleteAllQuiz();
  }


}
