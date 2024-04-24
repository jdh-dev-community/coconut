package com.coconut.quiz_spring.domain.quiz.controller;

import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizToJobPostingDto;
import com.coconut.quiz_spring.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_spring.domain.quiz.exception.InvalidOpenAiKeyException;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizAdminController.class)
@ActiveProfiles("test")
public class QuizAdminControllerTest {

  private final String baseUrl = "/api/v1/admin/quiz";

  @MockBean
  private QuizService quizService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Nested
  class 퀴즈생성_테스트 {

    private QuizDto quizDto;

    @BeforeEach
    public void setup() {
      quizDto = QuizDto.of(
              "Spring Framework에서 Dependency Injection이란 무엇인가요?",
              "Spring Framework, Dependency Injection, 객체, 의존 관계, 외부, 설정, 디자인 패턴, 객체 생성, 의존성 해결, 자동화, 코드 재사용성, 유지보수성",
              null
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

  @Nested
  class 퀴즈와채용공고매핑_테스트 {
    private String url = baseUrl + "/jobposting";

    private Map<String, Object> defaultRequest = Map.of(
            "jobPostingId", "1",
            "quizIds", List.of(1,2,3,4)
    );

    @Test
    public void 잘못된_요청을_하는_경우_400_반환() throws Exception {
      Map<String, String> invalidRequest = new HashMap();
      invalidRequest.put("jobPostingId", "1");
      invalidRequest.put("quizIds", "[1,2,3,4]");

      String invalidBody = objectMapper.writeValueAsString(invalidRequest);
      mockMvc.perform(post(url)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(invalidBody))
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 서비스_레이어에_dto를_전달한다() throws Exception {
      String body = objectMapper.writeValueAsString(defaultRequest);

      mockMvc.perform(post(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body));

      verify(quizService).mapQuizToJobPosting(
              argThat(dto ->
                      dto.getJobPostingId() == Long.parseLong((String) defaultRequest.get("jobPostingId"))
                              && dto.getQuizIds().size() == ((List<String>) defaultRequest.get("quizIds")).size()
              ));
    }

    @Test
    public void 성공시_200를_반환한다() throws Exception {
      String body = objectMapper.writeValueAsString(defaultRequest);

      mockMvc.perform(post(url)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(body))
              .andExpect(status().isOk());
    }

    @Test
    public void 성공시_맵핑된_목록을_반환한다() throws Exception {
      String body = objectMapper.writeValueAsString(defaultRequest);

      mockMvc.perform(post(url)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(body))
              .andExpect(jsonPath("$.result").isArray())
              .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    public void 서비스에서_예외가_발생할_경우_에러객체를_포함한다() throws Exception {
      String body = objectMapper.writeValueAsString(defaultRequest);

      when(quizService.mapQuizToJobPosting(any(QuizToJobPostingDto.class))).thenThrow(InvalidDataAccessApiUsageException.class);

      mockMvc.perform(post(url)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(body))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }
  }

}
