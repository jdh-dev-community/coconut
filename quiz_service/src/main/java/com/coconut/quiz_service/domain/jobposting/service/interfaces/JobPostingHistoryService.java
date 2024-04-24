package com.coconut.quiz_service.domain.jobposting.service.interfaces;

import com.coconut.quiz_service.domain.jobposting.dto.JobPostingHistoryCreateReq;

public interface JobPostingHistoryService {
  long createHistory(JobPostingHistoryCreateReq dto);
}
