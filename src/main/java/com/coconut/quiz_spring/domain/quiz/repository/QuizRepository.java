package com.coconut.quiz_spring.domain.quiz.repository;

import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
