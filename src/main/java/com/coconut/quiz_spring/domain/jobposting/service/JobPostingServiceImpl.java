package com.coconut.quiz_spring.domain.jobposting.service;

import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingAction;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.domain.mapper.JobPostingMapper;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingEditReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingHistoryCreateReq;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingHistoryService;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {

  private final JobPostingRepository jobPostingRepository;

  private final JobPostingMapper jobPostingMapper;

  private final JobPostingHistoryService jobPostingHistoryService;




  @Transactional
  @Override
  public JobPostingDto createJobPosting(JobPostingCreateReq dto) {
    JobPosting jobPosting = jobPostingMapper.from(dto);
    JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);


    logHistory("ToBeUserId", JobPostingAction.CREATE, savedJobPosting.getJobPostingId(), dto);

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

  @Transactional
  @Override
  public JobPostingDto editJobPosting(long jobPostingId, JobPostingEditReq dto) {
    if (dto == null) throw new IllegalArgumentException("dto는 null이 될 수 없습니다.");

    JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException("해당 id와 일치하는 공고가 없습니다. id: " + jobPostingId));

    jobPosting.updateJobPosting(
            dto.getTitle(),
            dto.getRequirements(),
            dto.getPreferred(),
            dto.getStack(),
            dto.getIcon(),
            dto.getStatus()
    );

    logHistory("ToBeUserId", JobPostingAction.EDIT, jobPosting.getJobPostingId(), dto);

    return JobPostingDto.from(jobPosting);
  }


  @Transactional
  @Override
  public JobPostingDto deleteJobPosting(long jobPostingId) {
    JobPosting jobPosting = jobPostingRepository.findByIdWithPessimisticWrite(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException());

    jobPosting.updateStatus(JobPostingStatus.DELETED);

    logHistory("ToBeUserId", JobPostingAction.DELETE, jobPostingId, null);

    return JobPostingDto.from(jobPosting);
  }

  private void logHistory(String actor, JobPostingAction action, long jobPostingId, Object dto) {
    try {
      JobPostingHistoryCreateReq history = JobPostingHistoryCreateReq.of(jobPostingId, actor, action, dto);
      jobPostingHistoryService.createHistory(history);
    } catch (RuntimeException ex) {
      log.error(ex.getMessage());
      log.info("로그 생성에 실패하였습니다.");
    }
  }

}
