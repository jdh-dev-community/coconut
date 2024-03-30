package com.coconut.quiz_spring.domain.quiz.repository;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  List<Quiz> findAllByJobPosting(JobPosting jobPosting);
}
