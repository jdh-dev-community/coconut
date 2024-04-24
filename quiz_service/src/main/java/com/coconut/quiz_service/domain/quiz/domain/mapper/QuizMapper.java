package com.coconut.quiz_service.domain.quiz.domain.mapper;

import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_service.domain.quiz.domain.Quiz;
import com.coconut.quiz_service.domain.quiz.dto.QuizDto;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

  public Quiz from(QuizDto dto) {
    return Quiz.builder()
            .quiz(dto.getQuiz())
            .keywords(dto.getKeywords())
            .build();
  }

  public Quiz from(QuizDto dto, JobPosting jobPosting) {
    return Quiz.builder()
            .quiz(dto.getQuiz())
            .keywords(dto.getKeywords())
            .jobPosting(jobPosting)
            .build();
  }
}
