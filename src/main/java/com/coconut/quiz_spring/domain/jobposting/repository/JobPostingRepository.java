package com.coconut.quiz_spring.domain.jobposting.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
}
