package com.coconut.quiz_spring.domain.jobposting.service;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPostingHistory;
import com.coconut.quiz_spring.domain.jobposting.domain.mapper.JobPostingHistoryMapper;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingHistoryCreateReq;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingHistoryRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class JobPostingHistoryServiceImpl implements JobPostingHistoryService {

  private final JobPostingHistoryRepository jobPostingHistoryRepository;

  private final JobPostingHistoryMapper jobPostingHistoryMapper;

  @Override
  public long createHistory(JobPostingHistoryCreateReq dto) {
    JobPostingHistory history = jobPostingHistoryMapper.from(dto);
    JobPostingHistory savedHistory =  jobPostingHistoryRepository.save(history);

    return savedHistory.getJobPostingHistoryId();
  }
}
