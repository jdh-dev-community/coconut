package com.coconut.quiz_spring.domain.quiz.dto;

import com.coconut.quiz_spring.domain.jobposting.dto.JobPostingDto;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizDto {

  @Schema(description = "퀴즈 id", example = "1")
  private long quizId;

  @Schema(description = "퀴즈 질문", example = "oop란 무엇인가요")
  private String quiz;

  @Schema(description = "추천 답안", example = "객체 지향의 ..")
  private String answer;

  @Schema(description = "핵심 키워드", example = "'srp, ocp,")
  private String keywords;

  @Schema(description = "연관 채용공고", example = "백엔드 주니어 채용공고")
  private JobPostingDto jobPosting;

  @JsonCreator
  public static QuizDto of(String quiz, String answer, String keywords) {
    return QuizDto.builder()
            .quiz(quiz)
            .answer(answer)
            .keywords(keywords)
            .build();
  }

  public void updateQuizId(long id) {
    this.quizId = id;
  }


  @Override
  public String toString() {
    return "QuizDto{" +
            "quiz='" + quiz + '\'' +
            ", answer='" + answer + '\'' +
            ", keywords='" + keywords + '\'' +
            '}';
  }
}
