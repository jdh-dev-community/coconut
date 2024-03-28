package com.coconut.quiz_spring.domain.jobposting.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
  @Lock(LockModeType.PESSIMISTIC_READ)
  @Query("SELECT j FROM JobPosting j WHERE jobposting_id = :jobPostId AND status = 'active'")
  Optional<JobPosting> findByIdWithPessimisticRead(@Param("jobPostId") long jobPostingId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT j FROM JobPosting j Where jobposting_id = :jobPostId AND status = 'active'")
  Optional<JobPosting> findByIdWithPessimisticWrite(@Param("jobPostId") long jobPostingId);

  @Query("SELECT j FROM JobPosting j WHERE j.status = 'active' AND (j.title LIKE CONCAT('%', :search, '%') OR j.stack LIKE CONCAT('%', :search, '%'))")
  Page<JobPosting> findAllActiveJobPostingWithSearch(Pageable pageable, @Param("search") String search);

  @Query("SELECT j FROM JobPosting j WHERE j.status = 'active'")
  Page<JobPosting> findAllActiveJobPosting(Pageable pageable);
}
