package com.coconut.quiz_spring.domain.interview.service.interfaces;


import com.coconut.quiz_spring.domain.interview.dto.InterviewCreateReq;
import com.coconut.quiz_spring.domain.interview.dto.InterviewDto;
import com.coconut.quiz_spring.domain.interview.dto.InterviewEditReq;
import org.springframework.stereotype.Service;



public interface InterviewService {

  InterviewDto createInterview(InterviewCreateReq dto);

  InterviewDto editInterview(InterviewEditReq dto);

  InterviewDto getInterview(long interviewId);

  void deleteInterview(long interviewId);

}
