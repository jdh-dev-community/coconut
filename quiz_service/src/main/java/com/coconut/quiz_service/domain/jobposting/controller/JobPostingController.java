package com.coconut.quiz_service.domain.jobposting.controller;

import com.coconut.quiz_service.common.dto.CustomResponse;
import com.coconut.quiz_service.common.dto.ListReqDto;
import com.coconut.quiz_service.common.dto.ListResDto;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_service.domain.jobposting.service.interfaces.JobPostingService;
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


  @Operation(summary = "채용공고 목록 조회", description = "채용공고 목록을 조회합니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/jobpostings")
  public CustomResponse<ListResDto<JobPostingDto>> getJobPostingList(@Valid @ModelAttribute ListReqDto listReqDto) {
    ListResDto<JobPostingDto> result = jobPostingService.getJobPostingList(listReqDto);
    CustomResponse<ListResDto<JobPostingDto>> response = CustomResponse.of(result);

    return response;
  }

  @Operation(summary = "채용공고 조회", description = "채용공고를 조회합니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/jobposting/{id}")
  public CustomResponse<JobPostingDto> getJobPosting(@PathVariable("id") long jobPostingId) {
    JobPostingDto result = jobPostingService.getJobPosting(jobPostingId);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;
  }

}
