package com.coconut.quiz_service.domain.quiz.service;

import com.coconut.quiz_service.domain.jobposting.constants.JobPostingStatus;
import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_service.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_service.domain.quiz.domain.Quiz;
import com.coconut.quiz_service.domain.quiz.dto.QuizDto;
import com.coconut.quiz_service.domain.quiz.dto.QuizToJobPostingDto;
import com.coconut.quiz_service.domain.quiz.repository.QuizRepository;
import com.coconut.quiz_service.domain.quiz.service.interfaces.QuizService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
public class QuizServiceTest {

  @Autowired
  private QuizService quizService;

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private JobPostingRepository jobPostingRepository;


  @AfterEach
  public void cleanup() {
    quizRepository.deleteAll();
  }


  @Nested
  class 퀴즈생성_테스트 {

    @Test
    public void 퀴즈생성에_성공하면_QuizDto를_반환한다() {
      QuizDto result = quizService.generateQuiz();

      assertThat(result.getQuizId()).isNotNull();
      assertThat(result.getQuiz()).isNotNull();
      assertThat(result.getKeywords()).isNotNull();
    }

    @Test
    public void 퀴즈생성에_성공하면_데이터베이스에_저장된다() {
      QuizDto result = quizService.generateQuiz();

      Quiz quiz = quizRepository.findById(result.getQuizId())
              .orElseThrow(() -> new EntityNotFoundException());

      assertEquals(quiz.getQuiz_id(), result.getQuizId());
      assertEquals(quiz.getQuiz(), result.getQuiz());
      assertEquals(quiz.getKeywords(), result.getKeywords());
    }

  }

  @Nested
  class 퀴즈매핑_테스트 {

    private JobPosting savedJobPosting;
    private Quiz savedQuiz;

    @BeforeEach
    public void setup() {
      JobPosting jobPosting = JobPosting.builder()
              .title("dummy")
              .requirements("자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 자바(Java) 프로그래밍 언어에 대한 실무 지식 \n 스프링 부트(Spring Boot)를 이용한 RESTful API 개발 경험 \n 객체 지향 프로그래밍(OOP) 원칙에 대한 이해 \n MySQL, PostgreSQL, MongoDB 등의 데이터베이스 관리 시스템에 대한 이해와 경험")
              .preferred("클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험\n 컨테이너화 도구(Docker, Kubernetes) 사용 경험\n CI/CD 파이프라인 구축 경험")
              .stack("springboot,java")
              .icon("/img/icon_spring.png")
              .status(JobPostingStatus.ACTIVE)
              .build();

      savedJobPosting = jobPostingRepository.save(jobPosting);

      Quiz quiz = Quiz.builder()
              .quiz("quiz")
              .keywords("1,2,3")
              .jobPosting(null)
              .build();

      savedQuiz = quizRepository.save(quiz);
    }

    @AfterEach
    public void cleanup() {
      quizRepository.deleteAll();
      jobPostingRepository.deleteAll();
    }

    @Test
    public void 퀴즈id_목록이_빈_목록일_경우_InvalidDataAccessApiUsageException() {
      QuizToJobPostingDto withEmptyQuizList = QuizToJobPostingDto.of(savedJobPosting.getJobPostingId(), null);
      assertThrows(InvalidDataAccessApiUsageException.class, () -> quizService.mapQuizToJobPosting(withEmptyQuizList));
    }

    @Test
    public void 일치하는_채용공고가_없는_경우_EntityNotFoundException() {
      QuizToJobPostingDto withNotMatchedJobPostingId = QuizToJobPostingDto.of(2, null);
      assertThrows(EntityNotFoundException.class, () -> quizService.mapQuizToJobPosting(withNotMatchedJobPostingId));
    }

    @Test
    public void 일치하는_퀴즈가_없는_경우_빈_목록_반환() {
      QuizToJobPostingDto withNotMatchedQuizId = QuizToJobPostingDto.of(savedJobPosting.getJobPostingId(), List.of(20L));
      List<QuizDto> result = quizService.mapQuizToJobPosting(withNotMatchedQuizId);

      assertThat(result).isEmpty();
    }

    @Test
    public void 퀴즈목록_매핑_성공시_성공한_QuizDto_반환() {
      QuizToJobPostingDto withMatchedQuizId = QuizToJobPostingDto.of(savedJobPosting.getJobPostingId(), List.of(savedQuiz.getQuiz_id()));
      List<QuizDto> result = quizService.mapQuizToJobPosting(withMatchedQuizId);

      assertThat(result).isNotEmpty();
      assertEquals(savedQuiz.getQuiz_id(), result.get(0).getQuizId());
      assertEquals(savedJobPosting.getJobPostingId(), result.get(0).getJobPostingId());

    }
  }
}
