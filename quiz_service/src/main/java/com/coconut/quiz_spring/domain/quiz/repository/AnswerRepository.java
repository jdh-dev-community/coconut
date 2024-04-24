package com.coconut.quiz_spring.domain.quiz.repository;

import com.coconut.quiz_spring.domain.quiz.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
