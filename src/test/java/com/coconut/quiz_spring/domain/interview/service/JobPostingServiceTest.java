package com.coconut.quiz_spring.domain.interview.service;

import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingHistoryRepository;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingHistoryService;
import com.coconut.quiz_spring.domain.jobposting.service.interfaces.JobPostingService;
import com.coconut.quiz_spring.testUtils.ConcurrencyTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class JobPostingServiceTest {

  @Autowired
  private JobPostingRepository jobPostingRepository;

  @Autowired
  private JobPostingService jobPostingService;

  @Autowired
  private JobPostingHistoryRepository jobPostingHistoryService;

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
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
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

      assertEquals(savedJobPosting.getJobPostingId(), result.getJobPostingId());
      assertEquals(savedJobPosting.getTitle(), result.getTitle());
      assertEquals(savedJobPosting.getRequirements(), result.getRequirements());
      assertEquals(savedJobPosting.getPreferred(), result.getPreferred());
      assertEquals(savedJobPosting.getStack(), result.getStack());
      assertEquals(savedJobPosting.getIcon(), result.getIcon());
      assertEquals(savedJobPosting.getViewCount() + 1, result.getViewCount());
      assertEquals(savedJobPosting.getStatus(), JobPostingStatus.ACTIVE);
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 주어진Id와_매칭되는_공고가_있는_경우_해당_공고의_조회수_1_증가() {
      long matchedId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.getJobPosting(matchedId);

      assertEquals(savedJobPosting.getViewCount() + 1, result.getViewCount());
    }

    @Test
    public void 동시에_채용공고_조회를_시도해도_조회수_일관성이_지켜진다() throws InterruptedException {
      long matchedId = savedJobPosting.getJobPostingId();
      int executeCount = 100;

      ConcurrencyTestUtils.executeConcurrently(() -> jobPostingService.getJobPosting(matchedId), executeCount);

      JobPostingDto result = jobPostingService.getJobPosting(matchedId);
      assertEquals(executeCount + 1, result.getViewCount());
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
      assertEquals(longDataDto.getTitle(), result.getTitle());
      assertEquals(longDataDto.getRequirements(), result.getRequirements());
      assertEquals(longDataDto.getPreferred(), result.getPreferred());
      assertEquals(longDataDto.getStack(), result.getStack());
      assertEquals(longDataDto.getIcon(), result.getIcon());
      assertEquals(0, result.getViewCount());
      assertEquals(JobPostingStatus.ACTIVE, result.getStatus());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 채용공고_생성에_성공한_경우_JobPostingDto를_반환() throws Exception {
      JobPostingDto result = jobPostingService.createJobPosting(createDto);

      assertThat(result.getJobPostingId()).isNotNull();
      assertEquals(createDto.getTitle(), result.getTitle());
      assertEquals(createDto.getRequirements(), result.getRequirements());
      assertEquals(createDto.getPreferred(), result.getPreferred());
      assertEquals(createDto.getStack(), result.getStack());
      assertEquals(createDto.getIcon(), result.getIcon());
      assertEquals(0, result.getViewCount());
      assertEquals(JobPostingStatus.ACTIVE, result.getStatus());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 생성된_채용공고가_데이터베이스에_저장된다() throws Exception {
      JobPostingDto result = jobPostingService.createJobPosting(createDto);
      JobPosting savedJobPosting = jobPostingRepository.findById(result.getJobPostingId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(savedJobPosting.getJobPostingId(), result.getJobPostingId());
    }

    @Test
    public void 채용공고_생성에_성공한_경우_history를_남긴다() {

    }

  }

  @Nested
  class 채용공고삭제_테스트 {
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
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
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
      assertThrows(EntityNotFoundException.class, () -> jobPostingService.deleteJobPosting(notMatchedId));
    }

    @Test
    public void 주어진Id와_매칭되는_공고가_있는_경우_stauts를_DELETED로_변경() {
      long matchedId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.deleteJobPosting(matchedId);
      JobPosting jobposting = jobPostingRepository.findById(matchedId)
              .orElseThrow(() -> new EntityNotFoundException());


      assertEquals(JobPostingStatus.DELETED, result.getStatus());
      assertEquals(jobposting.getStatus(), result.getStatus());
    }

    @Test
    public void 주어진Id와_매칭되는_공고가_있는_경우_status_변경_후_JobPostingDto_반환() {
      long matchedId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.deleteJobPosting(matchedId);

      assertEquals(savedJobPosting.getJobPostingId(), result.getJobPostingId());
      assertEquals(savedJobPosting.getTitle(), result.getTitle());
      assertEquals(savedJobPosting.getRequirements(), result.getRequirements());
      assertEquals(savedJobPosting.getPreferred(), result.getPreferred());
      assertEquals(savedJobPosting.getStack(), result.getStack());
      assertEquals(savedJobPosting.getIcon(), result.getIcon());
      assertEquals(savedJobPosting.getViewCount(), result.getViewCount());
      assertEquals(JobPostingStatus.DELETED, result.getStatus());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 여러개의_쓰레드에서_삭제를_시도할_경우_1번만_삭제되고_나머지는_예외를_반환한다() {
      long matchedId = savedJobPosting.getJobPostingId();

      int threadCount = 10;
      ExecutorService executor = Executors.newFixedThreadPool(threadCount);

      List<Future<JobPostingDto>> futures = IntStream.range(0, threadCount)
              .mapToObj((num) -> executor.submit(() -> jobPostingService.deleteJobPosting(matchedId)))
              .collect(Collectors.toList());

      executor.shutdown();

      int success = 0;
      int fail = 0;
      for (Future<JobPostingDto> future : futures) {
        try {
          future.get();
          success += 1;
        } catch (ExecutionException | InterruptedException e) {
          fail += 1;
        }
      }

      assertEquals(1, success);
      assertEquals(threadCount - success, fail);

    }
  }





}
