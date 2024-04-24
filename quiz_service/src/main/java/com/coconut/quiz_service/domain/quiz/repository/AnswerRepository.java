package com.coconut.quiz_service.domain.quiz.repository;

import com.coconut.quiz_service.domain.quiz.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
