package com.coconut.quiz_service.domain.jobposting.service.interfaces;


import com.coconut.jpa_utils.dto.ListReqDto;
import com.coconut.jpa_utils.dto.ListResDto;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingEditReq;


public interface JobPostingService {

  ListResDto<JobPostingDto> getJobPostingList(ListReqDto dto);
  JobPostingDto createJobPosting(JobPostingCreateReq dto);

  JobPostingDto editJobPosting(long jobPostingId, JobPostingEditReq dto);

  JobPostingDto getJobPosting(long jobPostingId);

  JobPostingDto deleteJobPosting(long jobPostingId);

  void deleteAllJobPosting();

}
