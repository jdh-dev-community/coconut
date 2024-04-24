package com.coconut.quiz_service.domain.quiz.domain;


import com.coconut.global.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity {

  @Schema(description = "id", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "answer_id", nullable = false)
  private long answerId;

  @Schema(description = "퀴즈id", example = "1")
  @Column(name = "quiz_id", nullable = false)
  private long quizId;

  @Schema(description = "채용공고id", example = "1")
  @Column(name = "jobposting_id")
  private Long jobPostingId;

  @Schema(description = "유저답변", example = "oo라고 생각합니다")
  @Column(name = "answer", nullable = false)
  private String answer;

  @Schema(description = "10점 만점 기준 획득점수", example = "10")
  @Column(name = "score", nullable = false)
  private int score;

  @Builder
  public Answer(long answerId, long quizId, long jobPostingId, String answer, int score) {
    this.answerId = answerId;
    this.quizId = quizId;
    this.jobPostingId = jobPostingId;
    this.answer = answer;
    this.score = score;
  }
}
