package com.coconut.quiz_spring.domain.quiz.service;


import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.OpenAiService;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

  private OpenAiService openAiService;

  @Override
  public QuizDto generateQuiz() {
    return openAiService.generateQuiz();
  }
}
