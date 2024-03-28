package com.coconut.quiz_spring.domain.jobposting.controller;

import com.coconut.quiz_spring.common.constant.OrderBy;
import com.coconut.quiz_spring.common.constant.SortBy;
import com.coconut.quiz_spring.common.dto.ListReqDto;
import com.coconut.quiz_spring.common.dto.ListResDto;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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
  class JobPostDetail_Test {

    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = createJobPostingDto(1);
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
  class JobPostCreate_Test {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = createJobPostingDto(1);
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
              .andExpect(jsonPath("$.error").isEmpty());
      ;

    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(baseUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }

  @Nested
  class JobPostingEdit_Test {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = createJobPostingDto(1);
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
      Map<String, String> validRequest = Map.of(
              "title", defaultRequest.get("title"),
              "status", "active"
      );
      String validBody = objectMapper.writeValueAsString(validRequest);

      when(jobPostingService.editJobPosting(eq(Long.parseLong(matchId)), any(JobPostingEditReq.class)))
              .thenReturn(jobPostingDto);

      patchAndVerify(matchId, validBody)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result").isNotEmpty())
              .andExpect(jsonPath("$.error").isEmpty());
      ;

    }

    private ResultActions patchAndVerify(String id, String body) throws Exception {
      return mockMvc.perform(patch(baseUrl + "/" + id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }

  @Nested
  class JobPostingDelete_Test {
    private JobPostingDto jobPostingDto;

    @BeforeEach
    public void setup() {
      this.jobPostingDto = createJobPostingDto(1);
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

  @Nested
  class JobPostList_Test {
    private String url = "/api/v1/jobpostings";
    private final int DEFAULT_PAGE_NUMBER = 1;
    private final int DEFAULT_PAGE_SIZE = 10;
    private final int MAX_PAGE_SIZE = 30;
    private final String DEFAULT_SORT_FIELD = "updatedAt";
    private final SortBy DEFAULT_SORT = SortBy.RECENT;
    private final OrderBy DEFAULT_ORDER = OrderBy.DESC;

    @Test
    public void 페이지_번호가_없는_경우_1을_기본값으로_사용한다() throws Exception {
      mockMvc.perform(get(url))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto -> listReqDto.getPage() == DEFAULT_PAGE_NUMBER)
      );
    }

    @Test
    public void 최소_페이지_번호는_1_이다_1미만은_400_응답을_한다() throws Exception {
      mockMvc.perform(get(url).param("page", "0"))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 페이지_사이즈가_없는_경우_10을_기본값으로_사용한다() throws Exception {
      mockMvc.perform(get(url))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto -> listReqDto.getSize() == DEFAULT_PAGE_SIZE)
      );
    }

    @Test
    public void 페이지_사이즈는_최대_30을_넘지_않는다() throws Exception {

      mockMvc.perform(get(url)
              .param("page", "1")
              .param("size", "60")
      ).andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto -> listReqDto.getSize() == MAX_PAGE_SIZE)
      );
    }

    @Test
    public void 정렬_기준이_없는_경우_updatedAt을_기준으로_사용한다() throws Exception {
      mockMvc.perform(get(url))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto ->
                      listReqDto.getSortBy().getFieldName() == DEFAULT_SORT_FIELD
                              && listReqDto.getSortBy() == DEFAULT_SORT)
      );
    }

    @Test
    public void 잘못된_정렬기준을_포함한_경우_400_응답을_한다() throws Exception {
      mockMvc.perform(get(url).param("sortBy", "random"))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 오름차순과_내림차순_기준이_없는_경우_DESC를_기준으로_사용한다() throws Exception {
      mockMvc.perform(get(url))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto -> listReqDto.getOrderBy() == DEFAULT_ORDER)
      );
    }

    @Test
    public void 잘못된_오름차순과_내림차순_기준을_포함한_경우_400_응답을_한다() throws Exception {
      mockMvc.perform(get(url).param("orderBy", "random"))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.result").isEmpty())
              .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void 검색어가_없는_경우_null_전달한다() throws Exception {
      mockMvc.perform(get(url))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto -> Objects.isNull(listReqDto.getSearch()))
      );
    }

    @Test
    public void 요청에_전달된_param을_서비스에_전달한다() throws Exception {
      mockMvc.perform(get(url)
                      .param("page", DEFAULT_PAGE_NUMBER + "")
                      .param("size", DEFAULT_PAGE_SIZE + "")
                      .param("sortBy", "recent")
                      .param("orderBy", "desc")
                      .param("search", "keywords"))
              .andExpect(status().isOk());

      verify(jobPostingService).getJobPostingList(
              argThat(listReqDto ->
                      listReqDto.getPage() == DEFAULT_PAGE_NUMBER
                              && listReqDto.getSize() == DEFAULT_PAGE_SIZE
                              && listReqDto.getSortBy().getSort().equals("recent")
                              && listReqDto.getOrderBy().getOrder().equals("desc")
                              && listReqDto.getSearch().equals("keywords")));
    }

    @Test
    public void 응답으로_채용공고_전체_숫자와_조회된_목록을_반환한다() throws Exception {
      int elementsCount = 10;

      List<JobPostingDto> dummyList = IntStream.rangeClosed(0, 3)
              .mapToObj((i) -> createJobPostingDto(i))
              .collect(Collectors.toList());

      ListResDto<JobPostingDto> result = ListResDto.of(elementsCount, dummyList);

      when(jobPostingService.getJobPostingList(any(ListReqDto.class))).thenReturn(result);

      mockMvc.perform(get(url))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.result.elementsCount", is(elementsCount)))
              .andExpect(jsonPath("$.result.content").isArray())
              .andExpect(jsonPath("$.error").isEmpty());

    }

  }

  private JobPostingDto createJobPostingDto(long id) {
    return JobPostingDto.of(
            id,
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
}
