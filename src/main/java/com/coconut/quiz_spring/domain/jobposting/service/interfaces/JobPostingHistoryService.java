package com.coconut.quiz_spring.domain.jobposting.service.interfaces;

import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingHistoryCreateReq;

public interface JobPostingHistoryService {
  long createHistory(JobPostingHistoryCreateReq dto);
}
