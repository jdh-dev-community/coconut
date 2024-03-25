package com.coconut.quiz_spring.domain.jobposting.service.interfaces;


import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingEditReq;


public interface JobPostingService {

  JobPostingDto createJobPosting(JobPostingCreateReq dto);

  JobPostingDto editJobPosting(JobPostingEditReq dto);

  JobPostingDto getJobPosting(long jobPostingId);

  void deleteJobPosting(long jobPostingId);

}
