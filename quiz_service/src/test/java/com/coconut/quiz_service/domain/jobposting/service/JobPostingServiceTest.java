package com.coconut.quiz_service.domain.jobposting.service;

import com.coconut.global.constant.OrderBy;
import com.coconut.global.constant.SortBy;
import com.coconut.jpa_utils.dto.ListReqDto;
import com.coconut.jpa_utils.dto.ListResDto;
import com.coconut.global.utils.CustomObjectMapper;
import com.coconut.quiz_service.domain.jobposting.constants.JobPostingAction;
import com.coconut.quiz_service.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_service.domain.jobposting.domain.JobPostingHistory;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingCreateReq;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_service.domain.jobposting.dto.JobPostingEditReq;
import com.coconut.quiz_service.domain.jobposting.repository.JobPostingHistoryRepository;
import com.coconut.quiz_service.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_service.domain.jobposting.service.interfaces.JobPostingService;
import com.coconut.quiz_service.utils.ConcurrencyTestUtils;
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
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JobPostingServiceTest {

  @Autowired
  private JobPostingRepository jobPostingRepository;

  @Autowired
  private JobPostingService jobPostingService;

  @Autowired
  private JobPostingHistoryRepository jobPostingHistoryRepository;

  @Autowired
  private CustomObjectMapper customObjectMapper;

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
      JobPosting jobPosting = createJobPosting(jobPostingData.get("title"));
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
      jobPostingHistoryRepository.deleteAll();
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
      JobPostingDto result = jobPostingService.createJobPosting(createDto);
      JobPostingHistory history = jobPostingHistoryRepository.findByJobPostingId(result.getJobPostingId())
              .orElseThrow(() -> new EntityNotFoundException());

      String request = customObjectMapper.writeValueAsString(createDto);

      assertEquals(JobPostingAction.CREATE, history.getAction());
      assertEquals(request, history.getRequest());
    }

  }

  @Nested
  class 채용공고수정_테스트 {

    private JobPosting savedJobPosting;
    private JobPostingEditReq editDto;

    @BeforeEach
    public void setup() {
      JobPosting jobPosting = createJobPosting(jobPostingData.get("title"));
      savedJobPosting = jobPostingRepository.save(jobPosting);

      this.editDto = JobPostingEditReq.of(
              jobPostingData.get("title"),
              null,
              null,
              "react",
              JobPostingStatus.INACTIVE.getValue(),
              jobPostingData.get("icon")
      );
    }

    @AfterEach
    public void cleanup() {
      jobPostingRepository.deleteAll();
    }

    @Test
    public void 수정하려하는_채용공고_id에_매치되는_공고가_없는_경우_EntityNotFoundException_발생() throws Exception {
      long notMatched = 100000;
      assertThrows(EntityNotFoundException.class, () ->jobPostingService.editJobPosting(notMatched, editDto));
    }
    @Test
    public void 잘못된_입력으로_채용공고_수정을_시도할_경우_IllegalArgumentException_발생() throws Exception {
      assertThrows(IllegalArgumentException.class, () -> jobPostingService.editJobPosting(savedJobPosting.getJobPostingId(), null));
    }

    @Test
    public void 수정을_요청한_필드에_대해서만_업데이트를_적용() throws Exception {
      long validId = savedJobPosting.getJobPostingId();
      JobPostingEditReq editDto = JobPostingEditReq.of(
              "new title",
              null,
              null,
              null,
              null,
              null
      );

      JobPostingDto result = jobPostingService.editJobPosting(validId, editDto);

      assertEquals(savedJobPosting.getJobPostingId(), result.getJobPostingId());
      assertEquals(editDto.getTitle(), result.getTitle());
      assertNotEquals(editDto.getRequirements(), result.getRequirements());
      assertNotEquals(editDto.getPreferred(), result.getPreferred());
      assertNotEquals(editDto.getStack(), result.getStack());
      assertNotEquals(editDto.getStatus(), result.getStatus());
      assertNotEquals(editDto.getIcon(), result.getIcon());

    }

    @Test
    public void 채용공고_수정에_성공한_경우_JobPostingDto를_반환() throws Exception {
      long validId = savedJobPosting.getJobPostingId();

      JobPostingDto result = jobPostingService.editJobPosting(validId, editDto);

      assertEquals(savedJobPosting.getJobPostingId(), result.getJobPostingId());
      assertEquals(editDto.getTitle(), result.getTitle());
      assertEquals(savedJobPosting.getRequirements(), result.getRequirements());
      assertEquals(savedJobPosting.getPreferred(), result.getPreferred());
      assertEquals(editDto.getStack(), result.getStack());
      assertEquals(editDto.getIcon(), result.getIcon());
      assertEquals(0, result.getViewCount());
      assertEquals(editDto.getStatus(), result.getStatus());
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 채용공고_수정에_성공한_경우_history를_남긴다() {
      long validId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.editJobPosting(validId, editDto);
      JobPostingHistory history = jobPostingHistoryRepository.findByJobPostingId(result.getJobPostingId())
              .orElseThrow(() -> new EntityNotFoundException());

      String request = customObjectMapper.writeValueAsString(editDto);

      assertEquals(validId, history.getJobPostingId());
      assertEquals(JobPostingAction.EDIT, history.getAction());
      assertEquals(request, history.getRequest());
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

    @Test
    public void 채용공고_삭제에_성공한_경우_history를_남긴다() {
      long validId = savedJobPosting.getJobPostingId();
      JobPostingDto result = jobPostingService.deleteJobPosting(validId);
      JobPostingHistory history = jobPostingHistoryRepository.findByJobPostingId(result.getJobPostingId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(validId, history.getJobPostingId());
      assertEquals(JobPostingAction.DELETE, history.getAction());
      assertNull(history.getRequest());
    }
  }

  @Nested
  class 채용공고목록_조회_테스트 {

    private int TOTAL_ITEM_COUNT = 40;


    @BeforeEach
    public void setup() {
      List<JobPosting> list = createJobPostingList(TOTAL_ITEM_COUNT);
      jobPostingRepository.saveAll(list);
    }

    @AfterEach
    public void cleanup() {
      jobPostingRepository.deleteAll();
    }

    @Test
    public void 조회된_채용공고의_숫자는_페이지_사이즈와_작거나_같다() {
      ListReqDto listReqDto = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.DESC, null);

      ListResDto<JobPostingDto> size10List = jobPostingService.getJobPostingList(listReqDto);
      assertThat(size10List.getContent().size()).isLessThanOrEqualTo(listReqDto.getSize());

      jobPostingRepository.deleteAll();
      List<JobPosting> list = createJobPostingList(3);
      jobPostingRepository.saveAll(list);

      ListResDto<JobPostingDto> size3List = jobPostingService.getJobPostingList(listReqDto);
      assertThat(size3List.getContent().size()).isLessThanOrEqualTo(listReqDto.getSize());
    }

    @Test
    public void 조회된_채용공는_페이지와_사이즈를_사용해서_페이징처리를_한다() {
      ListReqDto firstPageReq = ListReqDto.of(1, 3, SortBy.RECENT, OrderBy.DESC, null);
      ListResDto<JobPostingDto> firstPage = jobPostingService.getJobPostingList(firstPageReq);
      JobPostingDto firstPageLastItem = firstPage.getContent().get(firstPage.getContent().size() - 1);

      ListReqDto secondPageReq = ListReqDto.of(2, 3, SortBy.RECENT, OrderBy.DESC, null);
      ListResDto<JobPostingDto> secondPage = jobPostingService.getJobPostingList(secondPageReq);
      JobPostingDto secondPageFirstItem = secondPage.getContent().get(0);

      assertEquals(firstPageLastItem.getJobPostingId(), secondPageFirstItem.getJobPostingId() + 1);
    }

    @Test
    public void 정렬기준이_RECENT일_경우_수정일_기준으로_정렬한다() {
      ListReqDto firstPageReq = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.DESC, null);
      ListResDto<JobPostingDto> page = jobPostingService.getJobPostingList(firstPageReq);

      JobPostingDto firstItem = page.getContent().get(0);
      JobPostingDto secondItem = page.getContent().get(1);
      JobPostingDto thirdItem = page.getContent().get(2);

      assertThat(firstItem.getUpdatedAt()).isAfter(secondItem.getUpdatedAt());
      assertThat(secondItem.getUpdatedAt()).isAfter(thirdItem.getUpdatedAt());
    }

    @Test
    public void OrderBy가_ASC일_경우_수정일_기준으로_오름차순_정렬한다() {
      ListReqDto firstPageReq = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.ASC, null);
      ListResDto<JobPostingDto> page = jobPostingService.getJobPostingList(firstPageReq);

      JobPostingDto firstItem = page.getContent().get(0);
      JobPostingDto secondItem = page.getContent().get(1);
      JobPostingDto thirdItem = page.getContent().get(2);

      assertThat(firstItem.getUpdatedAt()).isBefore(secondItem.getUpdatedAt());
      assertThat(secondItem.getUpdatedAt()).isBefore(thirdItem.getUpdatedAt());
    }

    @Test
    public void 검색어가_있는_경우_제목_혹은_스택을_기준으로_매칭되는_모든_채용공고를_조회한다() {

      String searchKeyword = "검색키워드";
      JobPosting jobPosting = JobPosting.builder()
              .title(searchKeyword + "제목입니다.")
              .requirements(jobPostingData.get("requirements"))
              .preferred(jobPostingData.get("preferred"))
              .stack(searchKeyword + "스택입니다.")
              .icon(jobPostingData.get("icon"))
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
              .build();

      JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

      ListReqDto withSearch = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.ASC, searchKeyword);
      ListResDto<JobPostingDto> searchedPage = jobPostingService.getJobPostingList(withSearch);

      assertEquals(savedJobPosting.getJobPostingId(), searchedPage.getContent().get(0).getJobPostingId());
    }

    @Test
    public void 검색어가_있는_경우_제목을_기준으로_매칭되는_모든_채용공고를_조회한다() {

      String searchKeyword = "검색키워드";
      JobPosting jobPosting = JobPosting.builder()
              .title(searchKeyword + "제목입니다.")
              .requirements(jobPostingData.get("requirements"))
              .preferred(jobPostingData.get("preferred"))
              .stack(jobPostingData.get("stack"))
              .icon(jobPostingData.get("icon"))
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
              .build();

      JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

      ListReqDto withSearch = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.ASC, searchKeyword);
      ListResDto<JobPostingDto> searchedPage = jobPostingService.getJobPostingList(withSearch);

      assertEquals(savedJobPosting.getJobPostingId(), searchedPage.getContent().get(0).getJobPostingId());
    }

    @Test
    public void 검색어가_있는_경우_스택을_기준으로_매칭되는_모든_채용공고를_조회한다() {

      String searchKeyword = "검색키워드";
      JobPosting jobPosting = JobPosting.builder()
              .title(jobPostingData.get("title"))
              .requirements(jobPostingData.get("requirements"))
              .preferred(jobPostingData.get("preferred"))
              .stack("여기는 스택" + searchKeyword)
              .icon(jobPostingData.get("icon"))
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
              .build();

      JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

      ListReqDto withSearch = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.ASC, searchKeyword);
      ListResDto<JobPostingDto> searchedPage = jobPostingService.getJobPostingList(withSearch);

      assertEquals(savedJobPosting.getJobPostingId(), searchedPage.getContent().get(0).getJobPostingId());
    }

    @Test
    public void 검색어가_있지만_제목과_스택에_매칭되지않는다면_채용공고를_조회하지_않는다() {

      String searchKeyword = "검색키워드";
      JobPosting jobPosting = JobPosting.builder()
              .title(jobPostingData.get("title"))
              .requirements("제목과 스택에만 검색이 걸립니다" + searchKeyword)
              .preferred(jobPostingData.get("preferred"))
              .stack(jobPostingData.get("stack"))
              .icon(jobPostingData.get("icon"))
              .viewCount(0)
              .status(JobPostingStatus.ACTIVE)
              .build();

      JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

      ListReqDto withSearch = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.ASC, searchKeyword);
      ListResDto<JobPostingDto> searchedPage = jobPostingService.getJobPostingList(withSearch);

      assertEquals(0, searchedPage.getContent().size());
    }


    private List<JobPosting> createJobPostingList(int count) {
      return LongStream.rangeClosed(1, count)
              .mapToObj((i) -> createJobPosting("제목" + i))
              .collect(Collectors.toList());
    }

  }

  private JobPosting createJobPosting(String title) {
    return JobPosting.builder()
            .title(title)
            .requirements(jobPostingData.get("requirements"))
            .preferred(jobPostingData.get("preferred"))
            .stack(jobPostingData.get("stack"))
            .icon(jobPostingData.get("icon"))
            .viewCount(0)
            .status(JobPostingStatus.ACTIVE)
            .build();
  }

}
