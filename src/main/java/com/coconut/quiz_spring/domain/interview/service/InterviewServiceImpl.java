package com.coconut.quiz_spring.domain.interview.service;

import com.coconut.quiz_spring.domain.interview.domain.Interview;
import com.coconut.quiz_spring.domain.interview.domain.mapper.InterviewMapper;
import com.coconut.quiz_spring.domain.interview.dto.InterviewCreateReq;
import com.coconut.quiz_spring.domain.interview.dto.InterviewDto;
import com.coconut.quiz_spring.domain.interview.dto.InterviewEditReq;
import com.coconut.quiz_spring.domain.interview.repository.InterviewRepository;
import com.coconut.quiz_spring.domain.interview.service.interfaces.InterviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

  private final InterviewRepository interviewRepository;

  private final InterviewMapper interviewMapper;


  @Override
  public InterviewDto createInterview(InterviewCreateReq dto) {
    Interview interview = interviewMapper.from(dto);
    Interview savedInterview = interviewRepository.save(interview);

    return InterviewDto.from(savedInterview);
  }

  @Override
  public InterviewDto editInterview(InterviewEditReq dto) {
    return null;
  }

  @Override
  public InterviewDto getInterview(long interviewId) {
    return null;
  }

  @Override
  public void deleteInterview(long interviewId) {
  }
}
