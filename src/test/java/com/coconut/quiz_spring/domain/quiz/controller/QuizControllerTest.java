package com.coconut.quiz_spring.domain.quiz.controller;


import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_spring.domain.quiz.exception.InvalidOpenAiKeyException;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@ActiveProfiles("test")
public class QuizControllerTest {
  private final String baseUrl = "/api/v1/quiz";

  @MockBean
  private QuizService quizService;

  @Autowired
  private MockMvc mockMvc;

  @Nested
  class 퀴즈생성_테스트 {

    private QuizDto quizDto;

    @BeforeEach
    public void setup() {
      quizDto = QuizDto.of(
              "Spring Framework에서 Dependency Injection이란 무엇인가요?",
              "Dependency Injection은 객체 간의 의존 관계를 외부에서 설정해주는 디자인 패턴으로, 객체 생성 및 의존성 해결을 자동화하여 코드의 재사용성과 유지보수성을 향상시킵니다.",
              "Spring Framework, Dependency Injection, 객체, 의존 관계, 외부, 설정, 디자인 패턴, 객체 생성, 의존성 해결, 자동화, 코드 재사용성, 유지보수성"
      );

      quizDto.updateQuizId(1);
    }

    @Test
    public void 퀴즈생성에_성공하면_QuizDto와_201_응답() throws Exception {
      when(quizService.generateQuiz()).thenReturn(quizDto);

      mockMvc.perform(post(baseUrl))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.result.quizId", is(1)));
    }

    @Test
    public void 유효하지않은_apikey_때문에_실패할_경우_에러객체와_401_응답() throws Exception {
      when(quizService.generateQuiz()).thenThrow(InvalidOpenAiKeyException.class);

      mockMvc.perform(post(baseUrl))
              .andExpect(status().isUnauthorized())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 사용량_초과_때문에_실패할_경우_에러객체와_401_응답() throws Exception {
      when(quizService.generateQuiz()).thenThrow(ExceedOpenAiQuotaException.class);

      mockMvc.perform(post(baseUrl))
              .andExpect(status().isTooManyRequests())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }
  }
}
