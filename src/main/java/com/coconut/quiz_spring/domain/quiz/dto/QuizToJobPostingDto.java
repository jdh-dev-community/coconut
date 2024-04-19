package com.coconut.quiz_spring.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "퀴즈와 채용공고를 연결하기 위한 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizToJobPostingDto {

  private long jobPostingId;
  private List<Long> quizIds;

  @JsonCreator
  public static QuizToJobPostingDto of(long jobPostingId, List<Long> quizIds) {
    return QuizToJobPostingDto.builder()
            .jobPostingId(jobPostingId)
            .quizIds(quizIds)
            .build();
  }

  @Override
  public String toString() {
    return "QuizToJobPostingDto{" +
            "jobPostingId=" + jobPostingId +
            ", quizIds=" + quizIds +
            '}';
  }
}
