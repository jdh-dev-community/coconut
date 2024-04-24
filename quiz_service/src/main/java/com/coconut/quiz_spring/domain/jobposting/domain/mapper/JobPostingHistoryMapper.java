package com.coconut.quiz_spring.domain.jobposting.domain.mapper;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPostingHistory;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingHistoryCreateReq;
import org.springframework.stereotype.Component;

@Component
public class JobPostingHistoryMapper {
  public JobPostingHistory from (JobPostingHistoryCreateReq dto) {
    if (dto == null) throw new IllegalArgumentException("dto는 null이 될 수 없습니다.");

    return JobPostingHistory.builder()
            .jobPostingId(dto.getJobPostId())
            .actor(dto.getActor())
            .action(dto.getAction())
            .request(dto.getRequest())
            .build();
  }
}
