package com.coconut.quiz_spring.domain.jobposting.service;

import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingAction;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.domain.JobPostingHistory;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingHistoryCreateReq;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingHistoryRepository;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class JobPostingHistoryServiceTest {

  @Autowired
  private JobPostingRepository jobPostingRepository;

  @Autowired
  private JobPostingHistoryRepository jobPostingHistoryRepository;

  @Autowired
  private JobPostingHistoryService jobPostingHistoryService;

  @Autowired
  private ObjectMapper objectMapper;


  private Map<String, String> jobPostingData;
  private Map<String, String> jobPostingHistoryData;

  @BeforeEach
  public void setup() throws JsonProcessingException {

    jobPostingData = Map.of(
            "title", "테스트 면접",
            "requirements", "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험",
            "preferred", "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험\n 컨테이너화 도구(Docker, Kubernetes) 사용 경험\n CI/CD 파이프라인 구축 경험",
            "stack", "springboot,java",
            "icon", "/img/icon_spring.png"
    );


    jobPostingHistoryData = Map.of(
            "actor", "toBeUserId",
            "action", "create",
            "request", objectMapper.writeValueAsString(jobPostingData)
    );
  }

  @Nested
  class 채용공고_히스토리_생성_테스트 {

    private JobPosting savedJobPosting;
    private JobPostingHistoryCreateReq createDto;

    @BeforeEach
    public void setup() {
      JobPosting jobPosting = JobPosting.builder()
              .jobPostingId(1)
              .title(jobPostingData.get("title"))
              .requirements(jobPostingData.get("requirements"))
              .preferred(jobPostingData.get("preferred"))
              .stack(jobPostingData.get("stack"))
              .icon(jobPostingData.get("icon"))
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
              .build();

      savedJobPosting = jobPostingRepository.save(jobPosting);


      this.createDto = JobPostingHistoryCreateReq.of(
              savedJobPosting.getJobPostingId(),
              jobPostingHistoryData.get("actor"),
              JobPostingAction.findBy(jobPostingHistoryData.get("action")),
              jobPostingHistoryData.get("request")
      );
    }

    @AfterEach
    public void cleanup() {
      jobPostingRepository.deleteAll();
      jobPostingHistoryRepository.deleteAll();
    }

    @Test
    public void 잘못된_입력으로_채용공고_생성을_시도할_경우_IllegalArgumentException_발생() throws Exception {
      assertThrows(IllegalArgumentException.class, () -> jobPostingHistoryService.createHistory(null));
    }

    @Test
    public void 생성된_채용공고가_데이터베이스에_저장된다() throws Exception {
      long savedId = jobPostingHistoryService.createHistory(createDto);
      JobPostingHistory savedJobPostingHistory = jobPostingHistoryRepository.findById(savedId)
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(savedId, savedJobPostingHistory.getJobPostingHistoryId());
      assertEquals(savedJobPosting.getJobPostingId(), savedJobPostingHistory.getJobPostingId());
    }

  }
}
