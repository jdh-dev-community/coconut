package com.coconut.quiz_spring.domain.jobposting.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
  @Lock(LockModeType.PESSIMISTIC_READ)
  @Query("SELECT j FROM JobPosting j Where jobposting_id = :jobPostId")
  Optional<JobPosting> findByIdWithLock(@Param("jobPostId") long jobPostingId);
}
