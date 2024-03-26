package com.coconut.quiz_spring.domain.jobposting.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPostingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingHistoryRepository extends JpaRepository<JobPostingHistory, Long> {
}
