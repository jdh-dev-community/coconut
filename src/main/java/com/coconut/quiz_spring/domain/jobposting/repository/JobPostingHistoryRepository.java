package com.coconut.quiz_spring.domain.jobposting.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPostingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostingHistoryRepository extends JpaRepository<JobPostingHistory, Long> {
  Optional<JobPostingHistory> findByJobPostingId(long jobPostingId);
}
