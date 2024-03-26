package com.coconut.quiz_spring.domain.jobposting.controller;


import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
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
public class JobPostingController {

  private final JobPostingService jobPostingService;

  @GetMapping("/jobposting")
  public void getJobPostingList() {}

  @Operation(summary = "채용공고 조회", description = "채용공고를 조회합니다.")
  @GetMapping("/jobposting/{id}")
  public CustomResponse<JobPostingDto> getJobPosting(@PathVariable("id") long jobPostingId) {
    JobPostingDto result = jobPostingService.getJobPosting(jobPostingId);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;
  }


  @Operation(summary = "채용공고 생성", description = "채용공고을 생성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/jobposting")
  public CustomResponse<JobPostingDto> createJobPosting(@Valid @RequestBody JobPostingCreateReq dto) {
    JobPostingDto result = jobPostingService.createJobPosting(dto);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;
  }

  @PatchMapping("/jobposting")
  public void editJobPosting() {}

  @Operation(summary = "채용공고 삭제", description = "채용공고를 삭제합니다.")
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/jobposting/{id}")
  public CustomResponse<JobPostingDto> deleteJobPosting(@PathVariable("id") long jobPostingId) {
    JobPostingDto result = jobPostingService.deleteJobPosting(jobPostingId);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;

  }
}
