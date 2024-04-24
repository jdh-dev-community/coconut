package com.coconut.quiz_service.domain.quiz.service.interfaces;
import com.coconut.quiz_service.common.dto.ListReqDto;
import com.coconut.quiz_service.domain.quiz.dto.AnswerCreateReqDto;
import com.coconut.quiz_service.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizToJobPostingDto;

import java.util.List;

public interface QuizService {

  QuizDto insertExternalQuiz(QuizDto dto);
  QuizDto generateQuiz();
  List<QuizDto> mapQuizToJobPosting(QuizToJobPostingDto dto);
  List<QuizDto> findQuizByJobPostingId(long jobPostingId);
  AnswerDto createAnswer(AnswerCreateReqDto dto);
  List<QuizDto> getQuizList(ListReqDto dto);
  void deleteAllQuiz();
}
