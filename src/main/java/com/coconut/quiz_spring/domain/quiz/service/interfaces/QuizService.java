package com.coconut.quiz_spring.domain.quiz.service.interfaces;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizToJobPostingDto;

import java.util.List;

public interface QuizService {
  QuizDto generateQuiz();
  List<QuizDto> mapQuizToJobPosting(QuizToJobPostingDto dto);

  List<QuizDto> findQuizByJobPostingId(long jobPostingId);
}
