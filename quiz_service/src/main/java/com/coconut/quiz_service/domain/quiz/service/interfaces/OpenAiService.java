package com.coconut.quiz_service.domain.quiz.service.interfaces;

import com.coconut.quiz_service.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizDto;

public interface OpenAiService {
  QuizDto generateQuiz();
  AnswerDto generateAnswer(long quizId, String quiz, String keyword, String answer);
}
