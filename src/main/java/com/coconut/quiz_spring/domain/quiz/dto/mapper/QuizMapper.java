package com.coconut.quiz_spring.domain.quiz.dto.mapper;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class QuizMapper {

  public Quiz from(QuizDto dto) {
    return Quiz.builder()
            .quiz(dto.getQuiz())
            .keywords(dto.getKeywords())
            .answer(dto.getAnswer())
            .build();
  }

  public Quiz from(QuizDto dto, JobPosting jobPosting) {
    return Quiz.builder()
            .quiz(dto.getQuiz())
            .keywords(dto.getKeywords())
            .answer(dto.getAnswer())
            .jobPosting(jobPosting)
            .build();
  }
}
