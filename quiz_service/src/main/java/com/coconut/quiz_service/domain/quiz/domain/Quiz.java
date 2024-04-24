package com.coconut.quiz_service.domain.quiz.domain;

import com.coconut.global.domain.BaseEntity;
import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "quizzes")
public class Quiz extends BaseEntity {
  @Schema(description = "id", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "quiz_id")
  private long quiz_id;

  @Schema(description = "문제", example = "oop가 무엇인가요")
  @Column(name = "quiz")
  private String quiz;

  @Schema(description = "키워드", example = "'ocp','srp'")
  @Column(name = "keywords")
  private String keywords;

  @JoinColumn(name = "jobposting_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private JobPosting jobPosting;

  @Builder
  public Quiz(long quiz_id, String quiz, String keywords, JobPosting jobPosting) {
    this.quiz_id = quiz_id;
    this.quiz = quiz;
    this.keywords = keywords;
    this.jobPosting = jobPosting;
  }

  public void updateJobPosting(JobPosting jobPosting) {
    this.jobPosting = jobPosting;
  }

}
