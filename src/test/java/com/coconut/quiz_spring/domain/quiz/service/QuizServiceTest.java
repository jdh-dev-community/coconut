package com.coconut.quiz_spring.domain.quiz.service;

import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.repository.QuizRepository;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
public class QuizServiceTest {

  @Autowired
  private QuizService quizService;

  @Autowired
  private QuizRepository quizRepository;


  @AfterEach
  public void cleanup() {
    quizRepository.deleteAll();
  }


  @Nested
  class 퀴즈생성_테스트 {

    @Test
    public void 퀴즈생성에_성공하면_QuizDto를_반환한다() {
      QuizDto result = quizService.generateQuiz();

      assertThat(result.getQuizId()).isNotNull();
      assertThat(result.getQuiz()).isNotNull();
      assertThat(result.getAnswer()).isNotNull();
      assertThat(result.getKeywords()).isNotNull();
    }

    @Test
    public void 퀴즈생성에_성공하면_데이터베이스에_저장된다() {
      QuizDto result = quizService.generateQuiz();

      Quiz quiz = quizRepository.findById(result.getQuizId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(quiz.getQuiz_id(), result.getQuizId());
      assertEquals(quiz.getQuiz(), result.getQuiz());
      assertEquals(quiz.getAnswer(), result.getAnswer());
      assertEquals(quiz.getKeywords(), result.getKeywords());
    }

  }
}
