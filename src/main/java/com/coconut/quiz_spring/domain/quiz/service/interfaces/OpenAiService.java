package com.coconut.quiz_spring.domain.quiz.service.interfaces;

import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;

public interface OpenAiService {
  QuizDto generateQuiz();
  QuizDto generateAnswer(String prompt);
}
