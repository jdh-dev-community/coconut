package com.coconut.quiz_spring.domain.quiz.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerDto {

  @Schema(description = "퀴즈 id", example = "1")
  private long quizId;

  @Schema(description = "점수 (10점 만점으로 표기)", example = "1")
  private int score;

  @Schema(description = "이유", example = "1")
  private String reason;

  @Schema(description = "피드백", example = "1")
  private String feedback;

  @Schema(description = "핵심 키워드", example = "'srp, ocp,")
  private String keywords;

  @JsonCreator
  public static AnswerDto of(long quizId,  Integer score, String reason, String feedback, String keywords) {
    return AnswerDto.builder()
            .quizId(quizId)
            .score(score)
            .reason(reason)
            .feedback(feedback)
            .keywords(keywords)
            .build();
  }


}
