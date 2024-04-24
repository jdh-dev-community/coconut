package com.coconut.quiz_service.domain.quiz.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "퀴즈 정답 생성 요청을 위한 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerCreateReqDto {

  @NotNull
  private Long quizId;

  private Long jobPostingId;

  @NotEmpty
  private String answer;

  @JsonCreator
  public static AnswerCreateReqDto of(Long quizId, long jobPostingId, String answer) {
    return AnswerCreateReqDto.builder()
            .quizId(quizId)
            .jobPostingId(jobPostingId)
            .answer(answer)
            .build();
  }

  @Override
  public String toString() {
    return "AnswerCreateReqDto{" +
            "quizId=" + quizId +
            ", jobPostingId=" + jobPostingId +
            ", answer='" + answer + '\'' +
            '}';
  }
}
