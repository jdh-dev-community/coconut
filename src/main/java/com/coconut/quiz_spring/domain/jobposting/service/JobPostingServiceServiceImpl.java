package com.coconut.quiz_spring.domain.jobposting.service;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.domain.mapper.JobPostingMapper;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingEditReq;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostingServiceServiceImpl implements JobPostingService {

  private final JobPostingRepository jobPostingRepository;

  private final JobPostingMapper jobPostingMapper;


  @Override
  public JobPostingDto createJobPosting(JobPostingCreateReq dto) {
    JobPosting jobPosting = jobPostingMapper.from(dto);
    JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

    return JobPostingDto.from(savedJobPosting);
  }

  @Transactional
  @Override
  public JobPostingDto getJobPosting(long jobPostingId) {
    JobPosting jobPosting = jobPostingRepository.findByIdWithPessimisticRead(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException("해당 id와 일치하는 공고가 없습니다. id: " + jobPostingId));

    jobPosting.updateViewCount(jobPosting.getViewCount() + 1);

    return JobPostingDto.from(jobPosting);
  }

  @Override
  public JobPostingDto editJobPosting(JobPostingEditReq dto) {
    return null;
  }



  @Override
  public void deleteJobPosting(long jobPostingId) {
  }
}
