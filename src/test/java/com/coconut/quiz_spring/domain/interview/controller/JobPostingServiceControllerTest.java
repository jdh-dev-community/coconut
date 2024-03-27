package com.coconut.quiz_spring.domain.interview.controller;

import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_spring.domain.jobposting.controller.JobPostingController;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingEditReq;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(JobPostingController.class)
@ActiveProfiles("test")
public class JobPostingServiceControllerTest {
  private final String baseUrl = "/api/v1/jobposting";

  @MockBean
  private JobPostingService jobPostingService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  private Map<String, String> defaultRequest;

  @BeforeEach
  public void setup() {
    this.defaultRequest = Map.of(
            "title", "테스트 면접",
            "requirements", "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험",
            "preferred", "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험\n 컨테이너화 도구(Docker, Kubernetes) 사용 경험\n CI/CD 파이프라인 구축 경험",
            "stack", "springboot,java",
            "icon", "/img/icon_spring.png"
    );
  }

  @Nested
  class 채용공고조회_테스트 {

    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = JobPostingDto.of(
              1,
              defaultRequest.get("title"),
              defaultRequest.get("requirements"),
              defaultRequest.get("preferred"),
              defaultRequest.get("stack"),
              defaultRequest.get("icon"),
              1,
              JobPostingStatus.ACTIVE,
              LocalDateTime.now()
      );
    }

    @Test
    public void 채용공고Id가_숫자로_변환이_안되는_경우_400을_응답() throws Exception {
      String invalidId = "invalid";

      mockMvc.perform(get(baseUrl + "/" + invalidId))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 채용공고Id에_매칭되는_채용공고가_없는_경우_400을_응답() throws Exception {
      String notMatched = "-1";

      when(jobPostingService.getJobPosting(Long.parseLong(notMatched)))
              .thenThrow(EntityNotFoundException.class);

      mockMvc.perform(get(baseUrl + "/" + notMatched))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 채용공고Id에_매칭되는_채용공고가_있는_경우_채용공고와_200을_응답() throws Exception {
      String matched = "1";

      when(jobPostingService.getJobPosting(Long.parseLong(matched)))
              .thenReturn(jobPostingDto);

      mockMvc.perform(get(baseUrl + "/" + matched))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result").isNotEmpty())
              .andExpect(jsonPath("$.error").isEmpty());
    }
  }

  @Nested
  class 채용공고생성_테스트 {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = JobPostingDto.of(
              1,
              defaultRequest.get("title"),
              defaultRequest.get("requirements"),
              defaultRequest.get("preferred"),
              defaultRequest.get("stack"),
              defaultRequest.get("icon"),
              0,
              JobPostingStatus.ACTIVE,
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
    public void 요청_데이터가_유효한_경우_생성된_채용공고와_201_응답을_반환() throws Exception {
      Map<String, String> validRequest = defaultRequest;
      String validBody = objectMapper.writeValueAsString(validRequest);

      when(jobPostingService.createJobPosting(any(JobPostingCreateReq.class))).thenReturn(jobPostingDto);

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

  @Nested
  class 채용공고수정_테스트 {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {

      this.jobPostingDto = JobPostingDto.of(
              1,
              defaultRequest.get("title"),
              defaultRequest.get("requirements"),
              defaultRequest.get("preferred"),
              defaultRequest.get("stack"),
              defaultRequest.get("icon"),
              0,
              JobPostingStatus.ACTIVE,
              LocalDateTime.now()
      );
    }

    @Test
    public void 요청_데이터가_유효하지_않은_경우_400_응답을_반환() throws Exception {
      String matchId = "1";
      String invalidStatus = "wrong status";
      Map<String, String> invalidRequest = Map.of("status", invalidStatus);
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      patchAndVerify(matchId, invalidBody)
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 요청_데이터에_모든_항목이_누락된_경우_400_응답을_반환() throws Exception {
      String matchId = "1";
      Map<String, String> emptyRequest = new HashMap<>();
      String emptyBody = objectMapper.writeValueAsString(emptyRequest);

      patchAndVerify(matchId, emptyBody)
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 요청_데이터가_유효한_경우_수정된_채용공고와_200_응답을_반환() throws Exception {
      String matchId = "1";
      Map<String, String> validRequest = Map.of("title", defaultRequest.get("title"));
      String validBody = objectMapper.writeValueAsString(validRequest);

      when(jobPostingService.editJobPosting(eq(Long.parseLong(matchId)), any(JobPostingEditReq.class)))
              .thenReturn(jobPostingDto);

      patchAndVerify(matchId, validBody)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result").isNotEmpty())
              .andExpect(jsonPath("$.error").isEmpty());;

    }

    private ResultActions patchAndVerify(String id, String body) throws Exception {
      return mockMvc.perform(patch(baseUrl + "/" + id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }

  @Nested
  class 채용공고삭제_테스트 {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = JobPostingDto.of(
              1,
              defaultRequest.get("title"),
              defaultRequest.get("requirements"),
              defaultRequest.get("preferred"),
              defaultRequest.get("stack"),
              defaultRequest.get("icon"),
              0,
              JobPostingStatus.DELETED,
              LocalDateTime.now()
      );
    }

    @Test
    public void 채용공고Id가_숫자로_변환이_안되는_경우_400을_응답() throws Exception {
      String invalidId = "invalid";

      mockMvc.perform(delete(baseUrl + "/" + invalidId))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 채용공고Id에_매칭되는_채용공고가_없는_경우_400을_응답() throws Exception {
      String notMatched = "-1";

      when(jobPostingService.deleteJobPosting(Long.parseLong(notMatched)))
              .thenThrow(EntityNotFoundException.class);

      mockMvc.perform(delete(baseUrl + "/" + notMatched))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 채용공고Id에_매칭되는_삭제가능한_채용공고가_있는_경우_삭제처리_후_채용공고와_200을_응답() throws Exception {
      String matched = "1";

      when(jobPostingService.deleteJobPosting(Long.parseLong(matched)))
              .thenReturn(jobPostingDto);

      mockMvc.perform(delete(baseUrl + "/" + matched))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result").isNotEmpty())
              .andExpect(jsonPath("$.error").isEmpty());
    }

  }

}
