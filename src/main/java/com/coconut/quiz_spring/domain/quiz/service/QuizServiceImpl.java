package com.coconut.quiz_spring.domain.quiz.service;


import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.dto.mapper.QuizMapper;
import com.coconut.quiz_spring.domain.quiz.repository.QuizRepository;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.OpenAiService;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

  private final OpenAiService openAiService;

  private final QuizMapper quizMapper;

  private final QuizRepository quizRepository;

  @Override
  public QuizDto generateQuiz() {
    QuizDto result = openAiService.generateQuiz();
    Quiz quiz = quizMapper.from(result);
    quizRepository.save(quiz);

    result.updateQuizId(quiz.getQuiz_id());
    return result;
  }
}
