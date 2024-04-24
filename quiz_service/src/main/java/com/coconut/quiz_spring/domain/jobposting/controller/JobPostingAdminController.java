package com.coconut.quiz_spring.domain.jobposting.controller;

import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingEditReq;
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
@RequestMapping("/api/v1/admin")
public class JobPostingAdminController {

  private final JobPostingService jobPostingService;

  @Operation(summary = "모든 채용공고 삭제", description = "[주의] 모든 채용 공고 목록을 삭제합니다.")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/jobpostings")
  public void deleteAllJobPosting() {
    jobPostingService.deleteAllJobPosting();
  }

  @Operation(summary = "채용공고 생성", description = "채용공고을 생성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/jobposting")
  public CustomResponse<JobPostingDto> createJobPosting(@Valid @RequestBody JobPostingCreateReq dto) {
    JobPostingDto result = jobPostingService.createJobPosting(dto);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;
  }


  @Operation(summary = "채용공고 수정", description = "채용공고를 수정합니다.")
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/jobposting/{id}")
  public CustomResponse<JobPostingDto> editJobPosting(@PathVariable("id") long jobPostingId, @Valid @RequestBody JobPostingEditReq dto) {
    if (dto.isEmpty()) throw new IllegalArgumentException("수정하려는 데이터가 존재하지 않습니다.");

    JobPostingDto result = jobPostingService.editJobPosting(jobPostingId, dto);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;
  }


  @Operation(summary = "채용공고 삭제", description = "채용공고를 삭제합니다.")
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/jobposting/{id}")
  public CustomResponse<JobPostingDto> deleteJobPosting(@PathVariable("id") long jobPostingId) {
    JobPostingDto result = jobPostingService.deleteJobPosting(jobPostingId);
    CustomResponse<JobPostingDto> response = CustomResponse.of(result);

    return response;

  }
}
