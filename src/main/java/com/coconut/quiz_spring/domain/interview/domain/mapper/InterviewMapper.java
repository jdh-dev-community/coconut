package com.coconut.quiz_spring.domain.interview.domain.mapper;

import com.coconut.quiz_spring.domain.interview.domain.Interview;
import com.coconut.quiz_spring.domain.interview.dto.InterviewCreateReq;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {
  public Interview from(InterviewCreateReq dto) {
    if (dto == null) throw new IllegalArgumentException("dto는 null이 될 수 없습니다.");

    return Interview.builder()
            .title(dto.getTitle())
            .requirements(dto.getRequirements())
            .preferred(dto.getPreferred())
            .stack(dto.getStack())
            .icon(dto.getIcon())
            .build();
  }
}
