package com.coconut.quiz_spring.domain.interview.controller;

import com.coconut.quiz_spring.domain.interview.dto.InterviewCreateReq;
import com.coconut.quiz_spring.domain.interview.dto.InterviewDto;
import com.coconut.quiz_spring.domain.interview.service.interfaces.InterviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(InterviewController.class)
@ActiveProfiles("test")
public class InterviewControllerTest {
  private final String baseUrl = "/api/v1/interview";

  @MockBean
  private InterviewService interviewService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Nested
  class 인터뷰생성_테스트 {
    private Map<String, String> defaultRequest;
    private InterviewDto interviewDto;

    @BeforeEach
    public void setup() {
      this.defaultRequest = Map.of(
              "title", "테스트 면접",
              "requirements", "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험",
              "preferred", "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험\n 컨테이너화 도구(Docker, Kubernetes) 사용 경험\n CI/CD 파이프라인 구축 경험",
              "stack", "springboot,java",
              "icon", "/img/icon_spring.png"
      );

      this.interviewDto = InterviewDto.of(
              1,
              defaultRequest.get("title"),
              defaultRequest.get("requirements"),
              defaultRequest.get("preferred"),
              defaultRequest.get("stack"),
              defaultRequest.get("icon"),
              LocalDateTime.now()
      );
    }

    @Test
    public void 요청_데이터가_유효하지_않은_경우_400_응답을_반환() throws Exception {
      String invalidData = null;
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.put("title", invalidData);
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());

    }

    @Test
    public void 요청_데이터에_필수항목이_누락된_경우_Error객체와_400_응답을_반환() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.remove("title");

      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());

    }

    @Test
    public void 요청_데이터가_유효한_경우_생성된_인터뷰과_201_응답을_반환() throws Exception {
      Map<String, String> validRequest = defaultRequest;
      String validBody = objectMapper.writeValueAsString(validRequest);

      when(interviewService.createInterview(any(InterviewCreateReq.class))).thenReturn(interviewDto);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.result").isNotEmpty())
              .andExpect(jsonPath("$.error").isEmpty());;

    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(baseUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }
}
