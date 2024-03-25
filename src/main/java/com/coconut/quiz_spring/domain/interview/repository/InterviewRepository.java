package com.coconut.quiz_spring.domain.interview.repository;

import com.coconut.quiz_spring.domain.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
