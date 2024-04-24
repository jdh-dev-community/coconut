package com.coconut.quiz_service.domain.quiz.domain.mapper;

import com.coconut.quiz_service.domain.quiz.domain.Answer;
import com.coconut.quiz_service.domain.quiz.dto.AnswerCreateReqDto;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

  public Answer from (AnswerCreateReqDto dto, int score) {
    return Answer.builder()
            .quizId(dto.getQuizId())
            .jobPostingId(dto.getJobPostingId())
            .answer(dto.getAnswer())
            .score(score)
            .build();
  }
}
