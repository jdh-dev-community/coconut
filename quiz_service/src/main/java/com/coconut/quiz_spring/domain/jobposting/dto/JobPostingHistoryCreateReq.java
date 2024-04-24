package com.coconut.quiz_spring.domain.jobposting.dto;

import com.coconut.quiz_spring.common.utils.CustomObjectMapper;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingAction;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostingHistoryCreateReq {

  @NotNull
  private long jobPostId;

  @NotBlank
  private String actor;

  @NotNull
  private JobPostingAction action;

  private String request;

  public static JobPostingHistoryCreateReq of (long jobPostId, String actor, JobPostingAction action, Object dto) {
    CustomObjectMapper mapper = new CustomObjectMapper();

    JobPostingHistoryCreateReqBuilder builder = JobPostingHistoryCreateReq.builder()
            .jobPostId(jobPostId)
            .actor(actor)
            .action(action);

    if (dto != null) builder.request(mapper.writeValueAsString(dto));

    return builder.build();
  }
}
