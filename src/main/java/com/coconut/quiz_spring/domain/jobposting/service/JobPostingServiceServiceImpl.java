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

  @Override
  public JobPostingDto getJobPosting(long jobPostingId) {
    JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException());

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
