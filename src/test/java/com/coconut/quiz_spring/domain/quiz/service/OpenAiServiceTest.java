package com.coconut.quiz_spring.domain.quiz.service;

import com.coconut.quiz_spring.common.config.OpenAiProperties;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_spring.domain.quiz.exception.InvalidOpenAiKeyException;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.OpenAiService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class OpenAiServiceTest {

  @Autowired
  private OpenAiService openAiService;

  @MockBean
  private OpenAiProperties openAiProperties;

  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.api.url}")
  private String apiUrl;

  @Value("${openai.api.model}")
  private String apiModel;

  @Value("${openai.quiz.guide}")
  private String quizGuide;

  @Value("${openai.quiz.prompt}")
  private String quizPrompt;

  @Value("${openai.api.exceeded-key}")
  private String exceededKey;

  @Nested
  class 퀴즈생성_테스트 {

    @Test
    public void 성공시_퀴즈_QuizDto_반환() {
      when(openAiProperties.getApi()).thenReturn(new OpenAiProperties.Api(apiKey, apiUrl, apiModel));
      when(openAiProperties.getQuiz()).thenReturn(new OpenAiProperties.Quiz(quizGuide, quizPrompt));

      QuizDto result = openAiService.generateQuiz();

      assertThat(result.getQuiz()).isNotNull();
      assertThat(result.getKeywords()).isNotEmpty();
      assertThat(result.getKeywords()).isNotNull();
    }


    @Test
    public void 잘못된_api_key를_사용해서_요청을_시도한_경우_InvalidOpenAiKeyException() {
      String invalidKey = "test-key";

      when(openAiProperties.getApi()).thenReturn(new OpenAiProperties.Api(invalidKey, apiUrl, apiModel));
      when(openAiProperties.getQuiz()).thenReturn(new OpenAiProperties.Quiz(quizGuide, quizPrompt));

      assertThrows(InvalidOpenAiKeyException.class, () -> openAiService.generateQuiz());
    }

    @Test
    public void api_사용량을_모두_소진한_경우_ExceedOpenAiQuotaException() {
      when(openAiProperties.getApi()).thenReturn(new OpenAiProperties.Api(exceededKey, apiUrl, apiModel));
      when(openAiProperties.getQuiz()).thenReturn(new OpenAiProperties.Quiz(quizGuide, quizPrompt));

      assertThrows(ExceedOpenAiQuotaException.class, () -> openAiService.generateQuiz());
    }
  }

}
