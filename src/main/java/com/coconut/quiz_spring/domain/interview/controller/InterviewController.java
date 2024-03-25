package com.coconut.quiz_spring.domain.interview.controller;


import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.domain.interview.dto.InterviewCreateReq;
import com.coconut.quiz_spring.domain.interview.dto.InterviewDto;
import com.coconut.quiz_spring.domain.interview.service.interfaces.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class InterviewController {

  private final InterviewService interviewService;

  @GetMapping("/interview")
  public void getInterviewList() {}

  @GetMapping("/interview/{id}")
  public void getInterview() {}

  @Operation(summary = "인터뷰 생성", description = "채용공고을 생성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/interview")
  public CustomResponse<InterviewDto> createInterviewList(@Valid @RequestBody InterviewCreateReq dto) {
    InterviewDto result = interviewService.createInterview(dto);
    CustomResponse<InterviewDto> response = CustomResponse.of(result);

    return response;
  }

  @PatchMapping("/interview")
  public void editInterviewList() {}

  @DeleteMapping("/interview")
  public void deleteInterviewList() {}
}
