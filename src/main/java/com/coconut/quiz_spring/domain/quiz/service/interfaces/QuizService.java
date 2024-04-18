package com.coconut.quiz_spring.domain.quiz.service.interfaces;
import com.coconut.quiz_spring.common.dto.ListReqDto;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerCreateReqDto;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizToJobPostingDto;

import java.util.List;

public interface QuizService {

  QuizDto insertExternalQuiz(QuizDto dto);
  QuizDto generateQuiz();
  List<QuizDto> mapQuizToJobPosting(QuizToJobPostingDto dto);
  List<QuizDto> findQuizByJobPostingId(long jobPostingId);
  AnswerDto createAnswer(AnswerCreateReqDto dto);
  List<QuizDto> getQuizList(ListReqDto dto);
}
