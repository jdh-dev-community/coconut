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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  @Autowired
  private ObjectMapper objectMapper;


  @Nested
  class 채용공고Id로퀴즈조회_테스트 {
    private String url = baseUrl + "/jobposting";

    private List<QuizDto> quizDtos;

    @BeforeEach
    public void setup() {
      quizDtos = List.of(createQuizDto(1),createQuizDto(2));
    }


    @Test
    public void long으로_변환이_불가능한_id를_전달한_경우_400_응답() throws Exception {
      String invalidId = "A";

      mockMvc.perform(get(url + "/" + invalidId))
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 성공시_dto_list와_200을_반환() throws Exception {
      long validId = 1;
      long quizId = quizDtos.get(0).getQuizId();

      when(quizService.findQuizByJobPostingId(validId)).thenReturn(quizDtos);

      mockMvc.perform(get(url + "/" + validId))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result[0].quizId", is((int) quizId)))
              .andExpect(jsonPath("$.error").isEmpty());
    }



    private QuizDto createQuizDto(long id) {
      return QuizDto.builder()
              .quizId(id)
              .build();
    }
  }
}
