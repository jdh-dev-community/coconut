package com.coconut.quiz_service.domain.quiz.dto;

import com.coconut.quiz_service.domain.quiz.domain.Quiz;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizDto {

  @Schema(description = "퀴즈 id", example = "1")
  private long quizId;

  @Schema(description = "퀴즈 질문", example = "oop란 무엇인가요")
  private String quiz;

  @Schema(description = "핵심 키워드", example = "'srp, ocp,")
  private String keywords;

  @Schema(description = "연관 채용공고 id", example = "백엔드 주니어 채용공고")
  private Long jobPostingId;

  @JsonCreator
  public static QuizDto of(String quiz, String keywords, Long jobPostingId) {
    return QuizDto.builder()
            .quiz(quiz)
            .keywords(keywords)
            .jobPostingId(jobPostingId)
            .build();
  }

  public static QuizDto from (Quiz quiz) {
    Long jobPosingId = !Objects.isNull(quiz.getJobPosting())
            ? quiz.getJobPosting().getJobPostingId()
            : null;

    return QuizDto.builder()
            .quizId(quiz.getQuiz_id())
            .quiz(quiz.getQuiz())
            .keywords(quiz.getKeywords())
            .jobPostingId(jobPosingId)
            .build();
  }

  public void updateQuizId(long id) {
    this.quizId = id;
  }


  @Override
  public String toString() {
    return "QuizDto{" +
            "quiz='" + quiz + '\'' +
            ", keywords='" + keywords + '\'' +
            '}';
  }
}
