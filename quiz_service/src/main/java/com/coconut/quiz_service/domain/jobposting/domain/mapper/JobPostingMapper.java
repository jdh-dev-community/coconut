package com.coconut.quiz_service.domain.jobposting.domain.mapper;

import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingCreateReq;
import org.springframework.stereotype.Component;

@Component
public class JobPostingMapper {
  public JobPosting from(JobPostingCreateReq dto) {
    if (dto == null) throw new IllegalArgumentException("dto는 null이 될 수 없습니다.");

    return JobPosting.builder()
            .title(dto.getTitle())
            .requirements(dto.getRequirements())
            .preferred(dto.getPreferred())
            .stack(dto.getStack())
            .icon(dto.getIcon())
            .viewCount(0)
            .status(dto.getStatus())
            .build();
  }
}
