package com.coconut.quiz_spring.domain.interview.service;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobPostingServiceServiceTest {

  @Autowired
  private JobPostingRepository jobPostingRepository;

  @Autowired
  private JobPostingService jobPostingService;

  @Autowired
  private ObjectMapper objectMapper;

  private Map<String, String> jobPostingData;

  @BeforeEach
  public void setup() {
    jobPostingData = Map.of(
            "title", "테스트 면접",
            "requirements", "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험",
            "preferred", "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험\n 컨테이너화 도구(Docker, Kubernetes) 사용 경험\n CI/CD 파이프라인 구축 경험",
            "stack", "springboot,java",
            "icon", "/img/icon_spring.png"
    );
  }

  @Nested
  class 채용공고조회_테스트 {
    private JobPosting savedJobPosting;

    @BeforeEach
    public void setup() {
      JobPosting jobPosting = JobPosting.builder()
              .jobPostingId(1)
              .title(jobPostingData.get("title"))
              .requirements(jobPostingData.get("requirements"))
              .preferred(jobPostingData.get("preferred"))
              .stack(jobPostingData.get("stack"))
              .icon(jobPostingData.get("icon"))
              .build();

      savedJobPosting = jobPostingRepository.save(jobPosting);
    }

    @AfterEach
    public void cleanup() {
      jobPostingRepository.deleteAll();
    }

    @Test
    public void 주어진Id와_매칭되는_공고가_없는_경우_EntityNotFoundException() {
      long notMatchedId = -1;
      assertThrows(EntityNotFoundException.class, () -> jobPostingService.getJobPosting(notMatchedId));
    }

    @Test
    public void 주어진Id와_매칭되는_공고가_있는_경우_JobPostingDto_반환() {
      long matchedId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.getJobPosting(matchedId);

      assertThat(result.getJobPostingId()).isEqualTo(savedJobPosting.getJobPostingId());
      assertThat(result.getTitle()).isEqualTo(savedJobPosting.getTitle());
      assertThat(result.getRequirements()).isEqualTo(savedJobPosting.getRequirements());
      assertThat(result.getPreferred()).isEqualTo(savedJobPosting.getPreferred());
      assertThat(result.getStack()).isEqualTo(savedJobPosting.getStack());
      assertThat(result.getIcon()).isEqualTo(savedJobPosting.getIcon());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

  }

  @Nested
  class 채용공고생성_테스트 {
    private JobPostingCreateReq createDto;

    @BeforeEach
    public void setup() {
      this.createDto = JobPostingCreateReq.of(
              jobPostingData.get("title"),
              jobPostingData.get("requirements"),
              jobPostingData.get("preferred"),
              jobPostingData.get("stack"),
              jobPostingData.get("icon")
      );
    }

    @AfterEach
    public void cleanup() {
      jobPostingRepository.deleteAll();
    }

    @Test
    public void 잘못된_입력으로_채용공고_생성을_시도할_경우_IllegalArgumentException_발생() throws Exception {
      assertThrows(IllegalArgumentException.class, () -> jobPostingService.createJobPosting(null));
    }

    @Test
    public void 채용공고_생성을_위한_데이터가_255bytes가_초과해도_성공() throws Exception {
      String longData = "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험";
      JobPostingCreateReq longDataDto = JobPostingCreateReq.of(
              jobPostingData.get("title"),
              longData,
              jobPostingData.get("preferred"),
              jobPostingData.get("stack"),
              jobPostingData.get("icon")
      );

      JobPostingDto result = jobPostingService.createJobPosting(longDataDto);

      assertThat(result.getJobPostingId()).isNotNull();
      assertThat(result.getTitle()).isEqualTo(longDataDto.getTitle());
      assertThat(result.getRequirements()).isEqualTo(longDataDto.getRequirements());
      assertThat(result.getPreferred()).isEqualTo(longDataDto.getPreferred());
      assertThat(result.getStack()).isEqualTo(longDataDto.getStack());
      assertThat(result.getIcon()).isEqualTo(longDataDto.getIcon());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 채용공고_생성에_성공한_경우_JobPostingDto를_반환() throws Exception {
      JobPostingDto result = jobPostingService.createJobPosting(createDto);

      assertThat(result.getJobPostingId()).isNotNull();
      assertThat(result.getTitle()).isEqualTo(createDto.getTitle());
      assertThat(result.getRequirements()).isEqualTo(createDto.getRequirements());
      assertThat(result.getPreferred()).isEqualTo(createDto.getPreferred());
      assertThat(result.getStack()).isEqualTo(createDto.getStack());
      assertThat(result.getIcon()).isEqualTo(createDto.getIcon());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 생성된_채용공고가_데이터베이스에_저장된다() throws Exception {
      JobPostingDto result = jobPostingService.createJobPosting(createDto);
      JobPosting savedJobPosting = jobPostingRepository.findById(result.getJobPostingId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertThat(result.getJobPostingId()).isEqualTo(savedJobPosting.getJobPostingId());
    }

  }
}
